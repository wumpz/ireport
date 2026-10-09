import br.com.java_brasil.boleto.model.*;
import br.com.java_brasil.boleto.service.*;
import br.com.java_brasil.boleto.service.bancos.sicoob_cnab240.*;
import br.com.java_brasil.boleto.util.JasperUtil;
import java.nio.file.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/** Offline rendering only: never registers, sends, changes or pays a boleto. */
public class BoletoRuntimeSmoke {
  public static void main(String[] args)throws Exception {
    Endereco address=new Endereco();address.setLogradouro("RUA TESTE");address.setNumero("1");address.setBairro("TESTE");address.setCep("00000000");address.setCidade("CIDADE TESTE");address.setUf("SP");address.setComplemento("");
    Beneficiario owner=new Beneficiario();owner.setNomeBeneficiario("BENEFICIARIO TESTE - SEM VALIDADE");owner.setDocumento("00000000000000");owner.setAgencia("0001");owner.setDigitoAgencia("0");owner.setPostoDaAgencia("01");owner.setConta("000001");owner.setDigitoConta("0");owner.setCarteira("1");owner.setNumeroConvenio("0000001");owner.setEndereco(address);
    Pagador payer=new Pagador();payer.setNome("PAGADOR TESTE - SEM VALIDADE");payer.setDocumento("00000000000");payer.setEndereco(address);
    BoletoModel boleto=new BoletoModel();boleto.setBeneficiario(owner);boleto.setPagador(payer);boleto.setNossoNumero("000000001");boleto.setDigitoNossoNumero("0");boleto.setNumeroDocumento("TESTE-001");boleto.setNumeroBoleto("0000001");boleto.setDataEmissao(LocalDate.of(2026,10,6));boleto.setDataVencimento(LocalDate.of(2026,10,20));boleto.setValorBoleto(new BigDecimal("10.00"));boleto.setValorCobrado(new BigDecimal("10.00"));boleto.setEspecieDocumento("DM");boleto.setEspecieMoeda("R$");boleto.setInstrucoes(List.of());boleto.setDescricoes(List.of());boleto.setLocaisDePagamento(List.of(new InformacaoModel("TESTE SEM VALIDADE - NAO PAGAR")));boleto.setLocalPagamento("TESTE SEM VALIDADE - NAO PAGAR");
    boleto.setCodigoModalidade(1);
    BoletoService service=new BoletoService(BoletoBanco.SICOOB_CNAB240,new ConfiguracaoSicoobCnab240());
    byte[] pdf=service.imprimirBoletoJasper(boleto);
    if(pdf.length<1000)throw new AssertionError("Empty boleto PDF");
    Path out=Path.of(args[0]);Files.createDirectories(out);Files.write(out.resolve("boleto-sicoob-test.pdf"),pdf);
    byte[] merged=JasperUtil.unirRelatorio(List.of(pdf,pdf));Files.write(out.resolve("boleto-merged-test.pdf"),merged);
    try(var doc=org.apache.pdfbox.pdmodel.PDDocument.load(merged)) {
      String text=new org.apache.pdfbox.text.PDFTextStripper().getText(doc);
      if(doc.getNumberOfPages()!=2||!text.contains("PAGADOR TESTE")||!text.contains("BENEFICIARIO TESTE"))throw new AssertionError("Boleto content changed");
    }
    System.out.println("PASS offline Sicoob rendering, barcode and PDF merge; merged pages=2");
    if(args.length>1 && args[1].equals("source-build")) {
      Map<String,Object> fields=new HashMap<>();
      fields.put("beneficiario",owner);fields.put("pagador",payer);
      fields.put("nossoNumero",boleto.getNossoNumero());
      fields.put("documentoBeneficiario",owner.getDocumento());
      fields.put("dataVencimento",boleto.getDataVencimento());fields.put("dataEmissao",boleto.getDataEmissao());
      fields.put("valorBoleto",1000L);fields.put("valorCobrado",1000L);
      fields.put("valorMulta",0L);fields.put("valorDescontos",0L);
      fields.put("numeroDocumento","TESTE-001");fields.put("especieDocumento","DM");fields.put("especieMoeda","R$");
      fields.put("instrucoes",List.of("TESTE SEM VALIDADE - NAO PAGAR"));
      fields.put("localPagamento","TESTE SEM VALIDADE - NAO PAGAR");fields.put("carteira","1");fields.put("fatura","TESTE");
      fields.put("codigoBarras","0".repeat(44));
      net.sf.jasperreports.engine.JasperReport report;
      try(var input=BoletoRuntimeSmoke.class.getResourceAsStream("/Reports/boletoA4.jasper")) {
        if(input==null)throw new AssertionError("Missing packaged boletoA4");
        report=(net.sf.jasperreports.engine.JasperReport)net.sf.jasperreports.engine.util.JRLoader.loadObject(input);
      }
      var filled=net.sf.jasperreports.engine.JasperFillManager.fillReport(report,new HashMap<>(),new net.sf.jasperreports.engine.data.JRMapCollectionDataSource(List.of(fields)));
      byte[] rendered=net.sf.jasperreports.engine.JasperExportManager.exportReportToPdf(filled);
      Files.write(out.resolve("boletoA4-proposal-test.pdf"),rendered);
      try(var document=org.apache.pdfbox.pdmodel.PDDocument.load(rendered)) {
        String text=new org.apache.pdfbox.text.PDFTextStripper().getText(document);
        if(!text.contains("PAGADOR TESTE")||!text.contains("10,00")||text.split(boleto.getNossoNumero(),-1).length<3)
          throw new AssertionError("boletoA4 proposal lost number or amount");
      }
      System.out.println("PASS packaged boletoA4 proposal with synthetic cent amounts and nossoNumero on both copies");
    }
  }
}

