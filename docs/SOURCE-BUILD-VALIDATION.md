# Build isolado do ERP — 2026-10-07

## Escopo e proteção

Resultado consolidado: 27 registros de validação aprovados e 3.116 arquivos originais verificados sem alteração. O NBM final contém as mesmas 2.626 classes e 92 bibliotecas do perfil validado no NetBeans; o bytecode do módulo é compatível com Java 17. SHA-256 do NBM: `1f1e01d60dbc34523f3f74226ddb0809cb051b2665243b28f4c3198039ad0d21`.

O ERP original foi apenas lido. Fontes, relatórios e entradas necessárias foram copiados para `../laboratorio/erp-build-01`. A cópia não inclui certificados de produção; seu POM remove a execução de assinatura e os perfis externos. Os testes usam dados sintéticos, sem iniciar o ERP, acessar seu banco ou chamar serviços bancários.

`original-input-hashes.json` registra os SHA-256 de 3.116 arquivos de entrada. `Verify-IsolatedErpInputs.py` verifica os originais novamente. Alterações de build e de relatórios existem somente na cópia.

## Falhas encontradas e propostas

- A montagem extraía JasperReports e PDF antigos de `java-boletos`. A cópia exclui esses pacotes e agrega os serviços SPI; a auditoria confirma classes oficiais de JasperReports 6.21.4, Groovy 3.0.20 e OpenPDF 1.3.30.jaspersoft.3.
- O build copiava `.jasper` antigos e reaproveitava parte deles conforme suas datas. A cópia deixa de incluir esses binários como recursos e compila JRXML depois das classes Java do ERP.
- Faltava `barcode4j` no ambiente do compilador Maven e no runtime. As dependências de código de barras foram declaradas explicitamente na cópia.
- A exportação dos componentes SVG para PDF exigia Batik. Foram adicionados `batik-bridge` e `batik-svggen` 1.17, versões declaradas pelo JasperReports 6.21.4. As regras de redução preservam SVG, códigos de barras e acesso dinâmico dessas bibliotecas.
- A primeira redução pelo Maven removia `org.apache.xml.serializer.ToXMLStream`, carregada por reflexão ao serializar o código de barras SVG. A proposta preserva o serializador XML e as implementações Xalan/XPath. A falha foi observada em execução, apesar do build ProGuard ter terminado com sucesso.
- `boletoA4.jrxml` chamava `Beneficiario.getNossoNumero()`, método ausente na biblioteca atual. A proposta de laboratório usa um campo String `nossoNumero` em suas duas ocorrências. O teste fornece valores conforme o contrato declarado pelo modelo, incluindo quantias Long em centavos; ele não certifica um provedor real de dados. Não foi encontrada uma referência literal a `boletoA4` nas fontes do ERP.
- A cópia da venda recebe a proposta já validada para fazer quantidades e preços flutuarem abaixo da descrição longa. Os dois JRXML anteriores à proposta ficam em `report-proposals-before`, com hashes em `report-proposals.json`.

Correção de registro: o teste anterior dos 215 JRXML do designer verificava load/write/load/write; não era uma compilação integral. A compilação direta dos modelos originais com o primeiro runtime encontrou 213 aprovações e duas falhas (`boletoA4` e a dependência barcode4j de `Etiqueta_Transporte`), registradas em `corpus-real-compile.log`. Depois das propostas, o Maven compilou os 215 modelos, sem reaproveitar binários antigos.

## Verificações e evidências

- `build-corrected-tests.log`: recompilação dos 215 relatórios e 30 testes unitários aprovados, sem falhas, erros ou testes ignorados. Foram selecionados testes independentes do banco; a suíte inteira do ERP não foi executada.
- `build-svg-runtime.log`: build Maven completo com as dependências SVG; o JAR resulta das fontes da cópia, não de remontagem manual do JAR entregue.
- `build-clean-artifacts.log`: empacotamento final pelos plugins Maven jar/assembly, após retirar o diagnóstico da pasta de classes. `PackagedReportsAudit` também rejeita recursos inesperados na pasta Reports; os diagnósticos permanecem somente na pasta de evidências.
- `packaging-final-build.json`: identidade das classes centrais, ausência de entradas ZIP duplicadas, Main-Class preservado e SHA-256 do artefato.
- `PackagedReportsAudit`: todos os 215 relatórios do JAR devem ser idênticos aos binários do build e carregar no motor embarcado.
- `FinalJarReportSmoke`: venda, histórico e ficha de produto com seus subrelatórios; descrições longas sem sobreposição, dados acentuados, 0/1/80 itens, página contínua e paginação explícita de oito páginas.
- `BoletoRuntimeSmoke`: boleto Sicoob embarcado, código de barras e união de dois PDFs; adicionalmente, renderização da proposta `boletoA4` com nosso número em ambas as vias e valor de 10,00. Dados sem validade para pagamento.
- `BarcodeComponentsSmoke`: compilação e exportação de componentes Barbecue Code128, Barcode4j EAN13 e QR Code; leitura dos três códigos a partir do PDF rasterizado a 288 dpi. O rasterizador desativa antialiasing para evitar linhas artificiais entre módulos SVG. Esse teste não usa impressora ou leitor físico.
- Assinatura: certificado temporário autoassinado PKCS12 exclusivo do laboratório, SHA256withRSA, digest SHA-256. `jarsigner -verify` deve aprovar e os mesmos testes devem passar após assinatura. Avisos de confiança, validade curta e ausência de timestamp são esperados; nenhum certificado de produção ou TSA é utilizado.
- ProGuard: execução Maven `proguard@proguard-shrink`, depois dos testes do completo e da assinatura de laboratório. As regras originais já desativam obfuscação e otimização; portanto este teste valida redução, sem renomear classes. Os mesmos testes devem passar no small e em sua cópia assinada.

## Resultado

Resultado final do runtime: completo, completo assinado, small e small assinado passaram nos cinco grupos de testes descritos. As duas assinaturas foram verificadas. `final-results.json` consolida os registros, tamanhos e hashes, incluindo a transferência do relatório de códigos compilado no editor nativo para o small assinado em Java 17/Groovy 3.

## Designer: SVG e QR Code

O NBM anterior não incluía Batik nem ZXing. As mesmas bibliotecas SVG e ZXing 3.5.2 foram adicionadas ao POM do fork, sem alterações no ERP. `BarcodeExportCompatibilityTest` usa `IRLocalJasperReportsContext`, compila com a identificação padrão do JRGroovyCompiler, exporta o PDF e decodifica Code128, EAN13 e QR Code. PDFBox é somente dependência de teste e a auditoria do NBM confirma que não está no pacote.

Builds com JDK 21 e clean build JDK 25 passaram nos 10 testes do designer, incluindo o corpus de 215 JRXML em leitura/regravação. No perfil `userdir-svg-validation-nb31-jdk21`, o arquivo sintético abriu no editor visual, os três códigos foram exibidos, Preview compilou/preencheu com `Empty datasource` e mostrou uma página com os códigos. `Tools > Options > iReport` abriu corretamente; o diálogo foi cancelado sem salvar configurações.

O `.jasper` produzido nessa pré-visualização nativa foi carregado pelo JAR small assinado do source build, sem dependências do NetBeans e sem sobreposição externa de bibliotecas. Os três códigos foram novamente decodificados no PDF produzido: `native-barcode-small-signed.log`.

As advertências herdadas sobre persistência do MultiView e ordenação de menus permanecem; este teste não adiciona suporte a restauração de abas. O perfil diário do usuário não foi alterado.

## Comandos de reprodução

Preparar uma experiência nova com `Prepare-IsolatedErpBuild.py`, que recusa sobrescrever a existente. Aplicar as duas propostas apenas na cópia com `Apply-IsolatedReportCorrections.py`. No diretório da cópia, usar Java 17 e executar:

```powershell
mvn package -Dskip.proguard=true -Dskip.jarsigner=true -Dtest=TelefonesRelatorioTest,ValidaCPF_CNPJTest,ProdutoEstoqueConversaoTest,ProdutoConversaoCadastroValidatorTest,VendaEmAbertoPolicyTest,RenegociacaoCreditoFornecedorPolicyTest
```

Executar `Test-SourceBuildArtifacts.ps1 -Variant full`. Assinar uma cópia do JAR com certificado temporário e repetir com `-Variant full-signed`. Depois:

```powershell
mvn com.github.wvengen:proguard-maven-plugin:2.6.1:proguard@proguard-shrink -Dskip.proguard=false -Dskip.jarsigner=true
```

Executar `-Variant small`, assinar uma cópia do small e executar `-Variant small-signed`. Concluir com `Verify-IsolatedErpInputs.py`. Logs/PDFs/PNGs ficam fora do Git, na experiência de laboratório.

## Limites para produção

Os artefatos de laboratório não substituem o ERP original. Antes de distribuir, é necessário aplicar as propostas em uma mudança revisada, validar os fluxos da aplicação com seus provedores reais, verificar assinatura/certificado/TSA de produção e imprimir no equipamento afetado. Comprimento do papel, margens, corte e o rodapé observado na foto dependem dessa impressão. A execução do ERP com banco real e operações bancárias não faz parte destes testes offline.
