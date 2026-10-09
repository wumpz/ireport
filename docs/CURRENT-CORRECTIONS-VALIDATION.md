# Correções isoladas da entrega atual — 2026-10-08

O ERP original permanece preservado. A cópia `laboratorio/erp-build-current-02` contém as fontes e relatórios atuais do início desta rodada, não os arquivos antigos de erp-build-01. Alterações simultâneas em cinco arquivos não relacionados foram observadas no original e não foram sobrescritas. Os hashes dos originais relevantes (POM, ProGuard, assembly e dois relatórios envolvidos) continuam iguais aos registrados na cópia.

## Correções reproduzidas

- Barcode4J, Barbecue e Batik disponíveis na compilação e no runtime. A montagem exclui as versões antigas de JasperReports/PDF/Groovy embutidas em java-boletos e mescla descritores de serviços.
- boletoA4 usa o campo String nossoNumero nas duas ocorrências, em vez do método inexistente em Beneficiario. O teste usa o contrato declarado no JRXML, com valores Long em centavos; não certifica um provedor real nem altera esse contrato para BigDecimal.
- Quatro elementos de quantidade/preço do detalhe do modelo atual de 80 mm usam posição Float. Suas coordenadas atuais eram y=17; a correção anterior y=20 não se aplicava ao modelo atual. Foram preservadas as demais alterações do relatório atual.
- A compilação pelo plugin Maven legado emitia uma expressão Groovy que tratava Util como propriedade do script. O preenchimento falhava, embora a compilação fosse aprovada. Compilar os mesmos modelos em JVM separada com o classpath completo da aplicação resolve o caso; a montagem agora inclui esses binários novos.
- Regras conservadoras preservam dependências usadas por reflexão, Groovy, modelos de boleto, SVG e PDF. A experiência com otimização/ofuscação acrescenta ANTLR apenas para análise e mantém as APIs XML também fornecidas pela plataforma.

As propostas são reproduzíveis por Prepare-IsolatedErpBuild.py --destination seguido de Apply-CurrentIsolatedCorrections.py --build. Ambos restringem a escrita ao laboratório. A recompilação é uma tarefa local da cópia, por um helper sem inicialização do ERP ou serviços externos.

## Resultados concluídos

Build Maven aprovado: 215 relatórios compilados do zero após as classes da aplicação e 30 testes unitários offline, sem erros, falhas ou ignorados.

O JAR completo, sem bibliotecas externas de diagnóstico, passa nos seis grupos de Test-ProguardArtifact.ps1: 215 relatórios empacotados iguais às saídas da compilação e desserializáveis; venda/caixa/produto e subrelatórios; boleto/PDF merge; três códigos decodificados; layout com descrição longa; recuperação após três erros deliberados. A venda passa com 0/1/80 itens e oito páginas quando a paginação é solicitada.

O completo assinado com certificado temporário exclusivo do laboratório passa nos mesmos seis grupos. A verificação básica da assinatura passa, com avisos esperados de certificado autoassinado, validade curta e ausência de timestamp. Não é validação da assinatura de produção.

| Artefato de laboratório | Bytes | SHA-256 |
| --- | ---: | --- |
| Completo | 261371152 | fdddef2e01011d34dc8e879496c11b3e75c2253073e99d06d14f3205076d9a19 |
| Completo assinado temporariamente | 271558344 | 6453d4362906dff9d3f7698d9eb840033fe03375261a468388d2d43ba6942b6e |
| Otimizado e ofuscado | 182830836 | 60a206858b2945e38fe7cab1eac852cfc80b3b1e3c39f164b2296746994fcb55 |
| Otimizado/ofuscado assinado temporariamente | 188377587 | 0a866b0936f103f5ca467b10113dd09a960fde747ba7ea597bd99ea48f57c671 |

Experimento proguard-current-04 concluído em 8min27s: ProGuard 7.6.0 com otimização e ofuscação efetivas. O mapping confirma 22.851 classes renomeadas e nenhuma das APIs XML preservadas renomeada. O reduzido passa nos mesmos seis grupos, antes e depois da assinatura temporária; a verificação básica da assinatura também passa. Portanto os quatro artefatos desta rodada passam nos caminhos de relatório exercitados. Isso não certifica todas as funções da aplicação ofuscada.

## Limites da entrega

Esses artefatos não substituem o ERP real. A aplicação das propostas nos originais aguarda esclarecimento sobre a restrição anterior de não os alterar. O certificado da entrega real continua inadequado para assinatura de código. A execução ponta a ponta pelas telas, com provedores reais, não faz parte destes testes offline. Veja CURRENT-DELIVERY-VALIDATION.md para os JARs originais examinados.

## Distribuição do designer

A pré-release [ireport-jr6.21.4-netbeans31-rc1](https://github.com/samuelpc7/ireport/releases/tag/ireport-jr6.21.4-netbeans31-rc1) disponibiliza o NBM auditado com 91 dependências, pacote offline, fontes e hashes. Tamanhos e digests SHA-256 publicados pelo GitHub foram conferidos contra os arquivos locais. Consultas HEAD ao Maven Central retornaram 404 para as coordenadas históricas REx 0.8.1, SQLeonardo 2009.03.rc1 e Mondrian 3.2.0-13661-JS-3; seus binários estão no pacote. Isso não equivale a um espelho Maven completo para recompilar offline.
