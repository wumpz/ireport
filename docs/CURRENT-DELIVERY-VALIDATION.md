# Verificação adicional da entrega atual — 2026-10-08

**Resultado: os JARs atuais do ERP não estão aprovados para produção com a atualização.** O candidato isolado anteriormente validado continua distinto desses artefatos. Nenhum original foi alterado, recompilado ou reassinado nesta rodada.

## Artefatos examinados

Foram copiadas as duas entregas existentes em `AtheneSistema/target` para `laboratorio/release-check-2026-10-08`, além dos relatórios de `target/classes/Reports`. Os hashes dos originais ainda coincidem com as cópias ao final:

| Artefato | SHA-256 |
| --- | --- |
| AtheneSistema.jar | fea4e92abb4c7578b628ece659f32071b44497bfb781fa312c3c65d6a8be693f |
| AtheneSistema-small.jar | 97eff3224bdc24a4da2aedace29143a6d685ed14a89b15feb303d84e17c5ff3b |

## Resultados offline

| Verificação | Completo | Reduzido |
| --- | --- | --- |
| Verificação estrita da assinatura | Falha, código 12 | Falha, código 12 |
| Desserialização dos 215 relatórios empacotados | Falta Barcode4J ChecksumMode | Falta Barcode4J ChecksumMode |
| Venda, caixa, produto e subrelatórios sintéticos | Passa, inclusive 0/1/80 itens e oito páginas explícitas | Falha na inicialização Groovy VM_PLUGIN |
| Boleto | Sicoob offline/merge passam; boletoA4 falha em beneficiario.getNossoNumero() | NoSuchMethodError em Beneficiario.setPostoDaAgencia(String) no harness |
| Fixture dos três códigos | Falta Barcode4J ChecksumMode | Falta ZXing LuminanceSource na inicialização do harness |

O último resultado do reduzido, isoladamente, não demonstra que todos os relatórios do ERP usam ZXing: o harness também o usa para inspecionar os códigos. A falha independente de Barcode4J na desserialização dos relatórios empacotados confirma uma dependência necessária ausente.

Diagnóstico separado: acrescentando somente `barcode4j-2.1.jar` ao classpath do JAR completo, os 215 relatórios correspondem byte a byte às cópias de `target/classes/Reports` e desserializam. Isso confirma a dependência ausente; não corrige nem aprova o JAR entregue. Os logs ficam em `release-check-2026-10-08`.

`jarsigner -verify` básico informa assinatura verificada, porém com alertas. Com `-strict`, ambos falham: o certificado tem ExtendedKeyUsage que não permite assinatura de código e sua cadeia não foi validada pelo ambiente. A identidade e a chave privada não foram acessadas. Resolver confiança da cadeia não elimina a restrição de finalidade do certificado.

## Impressão direta e reversão

No candidato isolado otimizado/ofuscado e assinado temporariamente, foram preenchidos relatórios com dados sintéticos e enviados diretamente por `JRPrintServiceExporter`, usando configuração de serviço e diálogos desabilitados, como na API usada pelo ERP. Dois cupons, 60/80 mm, foram para Daruma; caixa A4 foi para Epson. Os três exports passam e os trabalhos são aceitos pelo spooler. As fotos enviadas pelo usuário foram conferidas sem corte aparente do conteúdo ou regressão do rodapé de paginação. **Não é um teste ponta a ponta pela tela do ERP.** Nenhum PDF intermediário foi usado na impressão direta.

Uma cópia descartável do perfil legado passou por migração seletiva e reversão offline. Os 315 arquivos iniciais foram restaurados byte a byte, incluindo os outros módulos e preferências. Isso valida a reversão dos arquivos, não a execução dos complementos antigos no NetBeans atual. O perfil diário não foi alterado.

## Homologação física já informada pelo usuário

- Medidas das bobinas de 60 e 80 mm corretas.
- Leitor LB-W retornou exatamente os 44 dígitos esperados do boleto após impressão via PDF com rasterização de 600 dpi. O driver não confirmou suporte ao atributo HIGH; não se deve equiparar essa rasterização à seleção da qualidade máxima no driver.
- QR do fixture retornou `TESTE SEM VALIDADE`, conteúdo esperado.
- O boleto sintético não continha dados de QR Pix; esse caminho não foi testado.

## Próximos passos para uma entrega real

Aplicar em uma entrega revisada as dependências, regras de preservação e correções de relatório já propostas no laboratório; resolver o certificado de assinatura; reconstruir e repetir os testes contra os novos hashes. Isso requer modificar a entrega do ERP, fora da autorização vigente de preservar seus originais. Permanecem específicos de ambiente a homologação pela tela do ERP, os provedores reais, o corte da bobina e eventuais complementos/Pix utilizados.
