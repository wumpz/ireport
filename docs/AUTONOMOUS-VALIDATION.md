# Testes autônomos adicionais — 2026-10-08

Atualização posterior: a migração seletiva e a correção do provedor XML passam; a atribuição inicial da falha apenas a bibliotecas antigas foi incompleta. O problema foi reproduzido com Woodstox atual. Resultados e candidato atualizado estão em SELECTIVE-MIGRATION-VALIDATION.md. O histórico das tentativas abaixo foi mantido para rastreabilidade.

O experimento ProGuard 03 também passou após completar a análise ANTLR e preservar APIs XML: seis grupos offline no JAR otimizado/ofuscado e os mesmos seis na cópia assinada com certificado temporário. Ver OBFUSCATION-VALIDATION.md para resultados e limites; a reprovação histórica abaixo se refere ao primeiro experimento.

Os testes usam o NBM e o build isolado validados em 2026-10-07. Nenhum comando deste trabalho altera o ERP original. Dados de conexão do usuário não foram copiados: o perfil usa preferências sintéticas e Empty datasource.

## Resultados confirmados

- Caminhos com espaços e acentos: os três pais (venda, caixa e produto) renderizam; caixa e produto carregam seus subrelatórios. Casos de venda com 0, 1 e 80 itens preservam os resultados anteriores, incluindo oito páginas ao habilitar paginação explicitamente. Evidência: `autonomous-2026-10-08/unicode-paths.log`.
- Erros do motor: JRXML inválido, dependência de expressão inexistente e subrelatório ausente são rejeitados. Na mesma JVM, um relatório válido compila, preenche e exporta PDF após as três falhas. Evidência: `failure-recovery.log`; teste reproduzível em `FailureRecoverySmoke.java`.
- Perfil migrado: depois da limpeza isolada descrita abaixo, o designer abre o fixture de três códigos de barras e gera preview. Options > iReport abre, preservando Groovy e cm. As cinco preferências sintéticas de linguagem, unidade, fonte, compilação de subrelatórios e diretório de saída permanecem após o encerramento normal e nova inicialização.
- Recuperação visual: na segunda inicialização, um JRXML inválido apresenta erro de validação e a árvore mostra Invalid report. Depois de fechar o diálogo e substituir apenas esse fixture descartável por XML válido, a aba XML carrega o conteúdo e Preview recompila e mostra os três códigos de barras na mesma sessão.

## Limitação encontrada na migração

A sobreposição offline do NBM sobre uma cópia do conjunto legado não passou diretamente. `components` e `jasperserver` antigos dependem de `com.jaspersoft.ireport/1`; o módulo moderno declara `com.jaspersoft.ireport`. A atualização desses complementos não faz parte do pacote validado.

Mesmo desativando os complementos, bibliotecas antigas remanescentes no perfil provocaram `XMLInputFactory`/`WstxInputFactory` ausente. A recuperação de laboratório colocou a pasta de módulos copiados em quarentena e extraiu somente os módulos/bibliotecas do NBM atual, mantendo as preferências sintéticas. Os registros anteriores à limpeza foram preservados em `upgrade-with-legacy-addons.log` e `upgrade-with-stale-libraries.log`.

Isso valida uma migração controlada do designer, não uma atualização completa pelo assistente Plugins do NetBeans. Para instalação de produção, use perfil limpo ou um procedimento que remova os arquivos pertencentes ao plugin antigo e trate os complementos separadamente. Não remova toda a pasta de módulos de um perfil de uso diário. A quarentena ampla deste teste foi limitada a uma cópia descartável.

## ProGuard e integridade

O experimento separado ativa otimização e ofuscação removendo `-dontoptimize`/`-dontobfuscate`, mantendo as regras de preservação da proposta anterior. Usa uma passagem de otimização e nomes sem mistura de maiúsculas/minúsculas. `Prepare-ProguardExperiment.ps1` prepara somente uma nova pasta dentro do laboratório. O build e seus resultados de execução devem ser avaliados antes de aprovar essa configuração; os JARs anteriormente validados não são substituídos.

Resultado: **BUILD FAILURE**, após 8min47s, durante a pré-verificação de `JROlapDataSource.init`. `MappingParser` depende de `antlr.LLkParser`, ausente no conjunto fornecido ao ProGuard; ele não consegue determinar a superclasse comum com `Mapping`. `-ignorewarnings` não corrige hierarquias incompletas. A configuração com otimização/ofuscação está reprovada neste teste; não houve JAR resultante para executar os testes de runtime. Não se deve desativar a pré-verificação como atalho para Java 17. A correção exige completar as dependências opcionais necessárias à análise e repetir build e execução; também permanece necessária a auditoria de recursos/reflexão após renomeação.

O registro está em `proguard-experiment/experiment.log`. A redução anterior, com `-dontoptimize` e `-dontobfuscate`, continua sendo a única configuração ProGuard validada. Os mapeamentos/diagnósticos do ERP permanecem fora do Git, dentro do laboratório.

A comparação com o snapshot de 2026-10-07 encontrou mudanças posteriores nos arquivos originais. Elas foram apenas registradas em `historical-input-differences.json`, sem reversão. Os resultados deste documento se referem à cópia do laboratório daquela data, não certificam as mudanças posteriores no ERP.

Impressão física, dados de produção, assinatura real e compatibilidade dos complementos legados continuam pendentes.
