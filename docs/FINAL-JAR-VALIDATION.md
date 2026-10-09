# Testes do JAR final — 06/10/2026

**Resultado: artefatos atuais reprovados para distribuição dos novos relatórios.** Nenhum arquivo do ERP foi alterado. Foram examinados os JARs já existentes em target, sem executar build, assinatura ou acesso ao banco. Hashes: laboratorio/final-jar-tests/tested-artifacts.csv. Conferência dos 3.102 arquivos protegidos: zero diferenças.

Ambiente Java 17. Dados sintéticos em Beans reais do ERP (Produto, GrupoProduto, Basic, UnidadeMedida, PedidoVenda, Cliente, Vendedor e Filial). Histórico usa duas instâncias de um filho; Ficha usa dois filhos diferentes, ambos com registros. Venda usa um item com quantidade 2 e preço 5. PDFs são verificados quanto a páginas e textos esperados, incluindo registros dos filhos e total 10,00. Harness: test-fixtures/engine/FinalJarReportSmoke.java.

## Resultados e causas

1. JAR completo e small, sem dependências externas: falham ao preencher os Jasper do editor (`Unkonwn class report32name`). O JRAbstractCompiler empacotado não corresponde ao oficial 6.21.4. Seu SHA coincide exatamente com a classe embutida em lib/java-boletos-1.0.8.jar. Isso ocorre antes de ProGuard.
2. Com JasperReports oficial 6.21.4 prioritário no classpath: completo preenche Histórico, mas exportação PDF falha com NoSuchMethodError Phrase.add(Element). A classe com.lowagie.text.Phrase empacotada também é idêntica à embutida em java-boletos. Small falha antes, na inicialização do Groovy VM_PLUGIN.
3. Com JasperReports 6.21.4, Groovy 3.0.20 e OpenPDF 1.3.30.jaspersoft.3 oficiais prioritários: Histórico e Ficha geram PDFs em ambos os artefatos. Venda compilada originalmente no editor falha ao resolver Util.MetodosUteis: o compilador visual não tinha as classes do ERP disponíveis e Groovy tratou Util como propriedade dinâmica.
4. Recompilação de seis cópias JRXML em laboratório com as classes do ERP visíveis: completo passa nos três modelos, gera três PDFs de uma página e preserva textos dos três filhos, dados principais e total esperado. Small passa Histórico/Ficha, mas falha em Venda: MissingMethodException formatarTelefonesRelatorio. javap confirma método presente no completo e ausente no small.
5. Cópia small-diagnostic.jar, exclusivamente em laboratório: restaurados MetodosUteis e classes internas a partir do completo, removidas assinaturas da cópia para não invalidar a identidade dos signatários, e usadas dependências oficiais prioritárias. Passa nos três modelos. Para inspeção automatizada dos PDFs, PDFBox/FontBox completos foram acrescentados; o small também perdeu PDDocument.load(File) e o provider XPath declarado no SPI. O teste de inspeção usou o provider XPath do JDK explicitamente. Essas adaptações são diagnóstico, não uma distribuição aprovada ou proposta de substituir o JAR assinado.

## Ajustes necessários antes de homologar

- Empacotamento: excluir JasperReports e classes PDF antigas incorporadas por java-boletos, garantindo motor 6.21.4 e biblioteca PDF compatível únicos. A mudança deve também testar emissão de boletos; simplesmente trocar com.lowagie pode afetar a biblioteca antiga.
- ProGuard: preservar classes Groovy carregadas por reflexão, métodos de Beans e auxiliares referenciados por expressões dos relatórios, incluindo formatarTelefonesRelatorio e dependências. Preservar providers declarados em META-INF/services. dontobfuscate não impede shrinking.
- Compilação visual: disponibilizar classes/dependências do ERP no classpath do iReport ao compilar expressões que chamam Util ou outras classes próprias. As seis cópias anteriores continuam preservadas, incluindo o binário produzido no editor.
- Repetir testes em artefato recém-gerado e assinado após ajustes; testar boletos, PDFs, impressão em impressora e casos reais de múltiplas páginas. Sem essas correções não é possível aprovar o JAR atual. O sucesso da conversão estrutural anterior não garantia preenchimento em runtime.

Evidências fora do Git: laboratorio/final-jar-tests/*.log, embedded-conflicts.json, erp-integrity.json, PDFs/textos em full-recompiled e small-diagnostic, binários recompilados em recompiled. Os únicos novos arquivos versionados são o harness e este registro; nenhum JAR de diagnóstico deve ser distribuído.
