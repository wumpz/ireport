# Otimização e ofuscação isoladas — 2026-10-08

Atualização: o experimento proguard-current-04 usa a cópia atual corrigida, conclui em 8min27s e passa nos seis grupos de runtime antes/depois da assinatura temporária. Renomeia 22.851 classes sem renomear APIs XML preservadas. Veja CURRENT-CORRECTIONS-VALIDATION.md para hashes e a correção adicional da compilação dos relatórios. Os resultados abaixo continuam como histórico do experimento anterior.

O terceiro experimento ProGuard 7.6.0 passou no build e nos seis grupos de execução offline, com Java 17. Usa o JAR do build isolado de 2026-10-07; não modifica o ERP original nem certifica alterações posteriores nele.

## Correções necessárias

1. A análise de OLAP requer ANTLR. Foi fornecido `antlr:antlr:2.7.5` como biblioteca de análise, versão declarada pelo POM do JasperReports 6.21.4. Isso não adiciona suporte OLAP ao runtime nem aprova essa funcionalidade opcional.
2. O primeiro build completo passou, mas o preenchimento falhou com `NoClassDefFoundError: javax/xml/parsers/f`. O mapeamento demonstrou que `ParserConfigurationException` havia sido renomeada. O fat JAR inclui 708 classes de APIs XML; a delegação ao JDK exige manter sua identidade. As regras experimentais preservam `javax.xml.**`, `org.w3c.dom.**` e `org.xml.sax.**`.

`Prepare-ProguardExperiment.ps1` reproduz a preparação em pasta nova de laboratório. Mantém as demais regras de reflexão anteriormente validadas, uma passagem de otimização, nomes únicos de membros e nomes sem mistura de maiúsculas/minúsculas. Não usa `-dontpreverify`. O build levou 7min36s.

## Resultado do terceiro experimento

- JAR: 182.814.614 bytes; SHA-256 `06e48f0e7fa0322452443bd485a233bdf89a527fb9480490c4c2aad2e9ae4b09`.
- 22.846 classes efetivamente renomeadas, nenhuma das três famílias de APIs XML acima.
- Os 215 relatórios empacotados correspondem aos binários compilados no build isolado e desserializam.
- Venda, caixa e produto, incluindo duas famílias de subrelatórios, preenchem/exportam com dados sintéticos.
- Venda com 0, 1 e 80 itens e oito páginas com paginação explícita passa; proposta de layout Float também passa.
- Boleto Sicoob offline, junção PDF e proposta boletoA4 sintética passam.
- CODE_128, EAN_13 e QR_CODE são decodificados do PDF gerado.
- XML inválido, dependência ausente e subrelatório ausente são rejeitados; a mesma JVM depois compila, preenche e exporta um relatório válido.
- Uma cópia assinada com o certificado temporário `lab-source-build` também passa nos seis grupos. `jarsigner -verify` retorna `jar verified`; cadeia autoassinada, validade curta e ausência de timestamp geram os avisos esperados. Certificado de produção não foi acessado. Evidências em `proguard-runtime-03-signed` e `proguard-experiment-03/verify-signing.log`.

Evidências locais: `autonomous-2026-10-08/proguard-experiment-03/proguard.log`, `artifact-audit.json`, `mapping.txt`, `platform-xml-duplicates.json` na pasta pai e os seis logs em `proguard-runtime-03`. `Test-ProguardArtifact.ps1` executa os grupos sem iniciar o ERP, banco ou serviços externos.

## Limites

Esses testes aprovam os caminhos exercitados de relatórios, não todas as telas e integrações do ERP sob ofuscação. O uso de `-ignorewarnings` não substitui auditoria completa de dependências opcionais. Antes de produção, revisar o mapeamento e as regras, validar os provedores reais e o certificado real. Os artefatos e mapeamentos ficam no laboratório e não são publicados no GitHub.
