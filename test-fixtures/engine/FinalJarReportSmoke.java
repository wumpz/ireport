import java.nio.file.*;
import java.util.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.*;
import net.sf.jasperreports.engine.util.JRLoader;

/** Uses only the delivered ERP jar and real ERP beans with synthetic values. */
public class FinalJarReportSmoke {
  static Object bean(String name) throws Exception {
    return Class.forName("Bean."+name).getConstructor(Integer.class).newInstance(1);
  }
  static void set(Object b,String name,Object value) throws Exception {
    var m=Arrays.stream(b.getClass().getMethods()).filter(x->x.getName().equals(name)&&x.getParameterCount()==1).findFirst().orElseThrow();
    m.invoke(b,value);
  }
  static void run(Path root,String name,Map<String,Object> p,JRDataSource ds,Path out) throws Exception {
    JasperReport r=(JasperReport)JRLoader.loadObject(root.resolve(name+".jasper").toFile());
    JasperPrint print=JasperFillManager.fillReport(r,p,ds);
    if(print.getPages().isEmpty())throw new AssertionError("No pages: "+name);
    JasperExportManager.exportReportToPdfFile(print,out.resolve(name+".pdf").toString());
    try(var pdf=org.apache.pdfbox.pdmodel.PDDocument.load(out.resolve(name+".pdf").toFile())) {
      String text=new org.apache.pdfbox.text.PDFTextStripper().getText(pdf);
      String[] required=name.startsWith("Historico")?new String[]{"ENTRADA TESTE","SAIDA TESTE"}:
        name.equals("Produto_Ficha")?new String[]{"PRODUTO TESTE","ACAO TESTE","MATERIAL TESTE"}:
        new String[]{"CLIENTE TESTE","ITEM TESTE","10,00"};
      for(String expected:required)if(!text.contains(expected))throw new AssertionError(name+" missing PDF text "+expected);
      Files.writeString(out.resolve(name+".txt"),text);
    }
    System.out.println("PASS "+name+" pages="+print.getPages().size());
  }
  public static void main(String[] args)throws Exception {
    Path root=Path.of(args[0]),out=Path.of(args[1]);Files.createDirectories(out);
    if(args.length>2 && args[2].equals("compile")) {
      try(var files=Files.list(root)) {for(Path f:files.filter(x->x.toString().endsWith(".jrxml")).toList())
        JasperCompileManager.compileReportToFile(f.toString(),out.resolve(f.getFileName().toString().replace(".jrxml",".jasper")).toString());}
      System.out.println("PASS compiled with ERP classes visible");return;
    }
    Map<String,Object> h=new HashMap<>();h.put("dt_inicial",new Date());h.put("dt_final",new Date());
    h.put("entradas",List.of(new Cash("ENTRADA TESTE",12.5)));h.put("saidas",List.of(new Cash("SAIDA TESTE",3.0)));
    run(root,"Historico_Caixa_Sintetico",h,new JREmptyDataSource(1),out);
    Object product=bean("Bean_Produto"),group=bean("Bean_GrupoProduto"),collection=bean("Bean_Basic");
    set(product,"setDescricao","PRODUTO TESTE");set(product,"setRef","TESTE");set(group,"setNomeGrupoProduto","GRUPO TESTE");
    set(collection,"setDescricao","COLECAO TESTE");set(product,"setObjGrupoProduto",group);set(product,"setObjColecao",collection);
    Object unit=bean("Bean_UnidadeMedida");set(unit,"setAbreviacao","UN");
    Map<String,Object> p=new HashMap<>();p.put("Produto",product);p.put("Tamanhos","P M G");
    p.put("Acoes",List.of(new Action()));p.put("MateriasPrimas",List.of(new Material(unit)));
    run(root,"Produto_Ficha",p,new JREmptyDataSource(1),out);
    Object order=bean("Bean_PedidoVenda"),client=bean("Bean_Cliente"),seller=bean("Bean_Vendedor"),branch=bean("Bean_Filial");
    set(branch,"setTitulo","FILIAL TESTE");set(client,"setNomeRazao","CLIENTE TESTE");set(seller,"setNome","VENDEDOR TESTE");
    set(order,"setObjFilial",branch);set(order,"setObjCliente",client);set(order,"setObjVendedor",seller);set(order,"setAcrescimo",0.0);set(order,"setDesconto",0.0);
    p=new HashMap<>();p.put("objPedidoVenda",order);p.put("ProdSemValor",false);p.put("obs_quantidades","");
    Map<String,Object> row=Map.of("id","1","quantidade_Total",2.0,"preco_Venda",new java.math.BigDecimal("5.00"),"ref","REF","descricao","ITEM TESTE","obs","");
    run(root,"Pedido_Venda_SemObs8Cm",p,new JRMapCollectionDataSource(List.of(row)),out);
    if(args.length>2 && args[2].startsWith("layout")) {
      for(int count:new int[]{0,1,80}) {
        List<Map<String,?>> rows=new ArrayList<>();
        for(int i=0;i<count;i++) {
          Map<String,Object> extended=new HashMap<>(row);
          extended.put("descricao","ITEM TESTE "+i+" DESCRICAO LONGA PARA VALIDAR QUEBRA DE LINHA E ACENTUACAO: ação, café, produção, São João.");
          rows.add(extended);
        }
        JasperReport report=(JasperReport)JRLoader.loadObject(root.resolve("Pedido_Venda_SemObs8Cm.jasper").toFile());
        JasperPrint print=JasperFillManager.fillReport(report,new HashMap<>(p),new JRMapCollectionDataSource(rows));
        if(args[2].equals("layout-fixed"))checkDescriptionOverlap(print);
        if(count>0 && print.getPages().isEmpty())throw new AssertionError("No layout pages");
        if(count==80 && print.getPages().size()!=1)throw new AssertionError("Thermal continuous page changed");
        if(!print.getPages().isEmpty()) {
          Path pdfPath=out.resolve("sale-layout-"+count+".pdf");
          JasperExportManager.exportReportToPdfFile(print,pdfPath.toString());
          try(var pdf=org.apache.pdfbox.pdmodel.PDDocument.load(pdfPath.toFile())) {
            String text=new org.apache.pdfbox.text.PDFTextStripper().getText(pdf);
            if(count>0 && (!text.contains("ITEM TESTE 0") || !text.contains("São João")))throw new AssertionError("Lost wrapped text");
            if(count==80 && !text.contains("ITEM TESTE 79"))throw new AssertionError("Lost final item");
          }
        }
        System.out.println("PASS sale layout rows="+count+" pages="+print.getPages().size());
        if(count==80) {
          Map<String,Object> paginated=new HashMap<>(p);
          paginated.put(JRParameter.IS_IGNORE_PAGINATION,false);
          JasperPrint paged=JasperFillManager.fillReport(report,paginated,new JRMapCollectionDataSource(rows));
          if(args[2].equals("layout-fixed"))checkDescriptionOverlap(paged);
          if(paged.getPages().size()<2)throw new AssertionError("Explicit pagination did not paginate");
          JasperExportManager.exportReportToPdfFile(paged,out.resolve("sale-layout-80-paginated.pdf").toString());
          System.out.println("PASS explicit sale pagination pages="+paged.getPages().size());
        }
      }
    }
  }
  static void checkDescriptionOverlap(JasperPrint print) {
    for(JRPrintPage page:print.getPages()) {
      for(JRPrintElement element:page.getElements()) {
        if(!(element instanceof JRPrintText description) || !description.getFullText().startsWith("1-ITEM TESTE"))continue;
        for(JRPrintElement other:page.getElements()) {
          if(other instanceof JRPrintText price && price.getFullText().contains("R$")
              && other.getY()>=element.getY() && other.getY()<element.getY()+element.getHeight())
            throw new AssertionError("Description overlaps price at y="+other.getY());
        }
      }
    }
  }
  public static class Cash {String d;double v;Cash(String d,double v){this.d=d;this.v=v;}public String getDescricao(){return d;}public Double getValor(){return v;}public String getObs(){return "";}public String getTipo(){return "TESTE";}}
  public static class Action {public String getId(){return "1";}public String getDescricao(){return "ACAO TESTE";}public Double getQuantidade(){return 2.0;}public Double getValor(){return 4.0;}}
  public static class Material {Object unit;Material(Object u){unit=u;}public Integer getId(){return 1;}public String getDescricao(){return "MATERIAL TESTE";}public Double getQtd_A_Movimentar(){return 2.0;}public Double getPreco_Custo(){return 3.0;}public String getReferencia(){return "REF";}public Object getObjUnidadeMedida(){return unit;}public Object getObjGrupoMateriaPrima(){return null;}}
}
