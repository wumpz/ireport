# Validação JRXML → Jasper → JRXML — 06/10/2026

Ambiente: Apache NetBeans 31, JDK 21, fork com JasperReports 6.21.4 e correção de Options do commit d177552. Perfil isolado `userdir-options-fixed-nb31-jdk21`.

Foram usados três modelos principais copiados do snapshot do ERP: Pedido_Venda_SemObs8Cm, Historico_Caixa_Sintetico e Produto_Ficha. Histórico contém duas instâncias do subrelatório Entradas; Ficha contém AcoesProducao e MatPrima. Total: seis arquivos independentes.

Em cada cópia foi alterado o primeiro textField para `textAdjust="StretchHeight"`, o reportElement para `stretchType="ContainerHeight"` e a fonte para `size="10.5"`. São recursos do motor moderno que o editor legado não suporta integralmente. As cópias anteriores e modificadas foram preservadas em `original` e `reference`.

Os três principais foram abertos no Designer e compilados pelo botão de compilação do iReport. Seus filhos foram compilados automaticamente. Todos os seis Jasper foram gerados sem erro. Antes da exclusão, os seis JRXML de trabalho tinham SHA-256 idêntico às referências modificadas.

As abas foram fechadas e somente os seis JRXML de `laboratorio/roundtrip-modern-01/work` foram apagados. Cada Jasper foi aberto pela árvore Favorites, convertido pelo diálogo nativo e reaberto automaticamente no Designer. Os seis abriram normalmente, inclusive os filhos.

Resultado: **6/6 estruturas completas preservadas**. O auditor independente carregou referência e arquivo reconstruído com JRXmlLoader e binário com JRLoader, normalizando os três através de JRXmlWriter. A comparação verificou todos os elementos, sua ordem, atributos e textos significativos, incluindo expressões, parâmetros, variáveis, bandas e subrelatórios. Apenas comentários, indentação e ordem lexical dos atributos não participam da comparação. Nenhuma propriedade semântica foi ignorada. Os três recursos modernos permaneceram nos seis arquivos.

Evidências locais: `../laboratorio/roundtrip-modern-01/evidence/comparison.json`, `audit.log`, `deleted-sources.json`, XML normalizados antes/depois/binário e `erp-integrity.json`. O auditor executou com Java 17 e classpath sem módulos NetBeans; os binários registram o compilador padrão JRGroovyCompiler.

Limites: esta validação comprova edição, compilação e conversão estrutural. Não executa preenchimento com dados reais nem compara impressão física ou PDFs destes modelos. File → Open File não abriu os arquivos adicionais nesta sessão; Favorites e abertura automática após conversão funcionaram. A causa desse comportamento de Open File continua pendente.

Reprodução: Prepare-RoundTripFixtures.py cria uma experiência nova sem sobrescrever a existente; repetir as ações visuais descritas; compilar e executar test-fixtures/engine/ReportRoundTripAudit.java com o classpath JasperReports do teste Maven; executar Compare-RoundTripFixtures.py. ERP e relatórios originais são apenas referência de leitura.
