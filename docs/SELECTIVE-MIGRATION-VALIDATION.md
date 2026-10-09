# Migração seletiva e correção StAX — 2026-10-08

O candidato do designer passou em NetBeans 31/Java 21, usando um perfil descartável construído a partir dos módulos antigos. Nenhum arquivo do ERP original foi editado.

## Correção e evidência

A primeira limpeza seletiva reproduziu `XMLInputFactory: Provider com.ctc.wstx.stax.WstxInputFactory not found`. O problema também acontece com o Woodstox atual: seu registro de serviço é encontrado pelo código de ícones SVG do NetBeans, mas o provedor privado não fica acessível ao carregador desse consumidor. Exportar os pacotes do provedor não resolveu o teste nativo.

O POM do designer agora exclui Woodstox das duas origens transitivas, JasperReports e JAX-WS, usando StAX do JDK. O teste XML verifica caracteres acentuados, entidades, serialização Jackson e leitura offline de envelope SOAP. Isso não certifica conexão real com JasperServer. Separadores duplicados no layer também receberam nomes distintos.

- Java 21 e Java 25: **12 testes, zero falhas, zero erros, zero ignorados**. O teste de corpus carrega e reconverte XML de 215 JRXMLs da cópia do laboratório; a compilação efetiva desses relatórios está documentada separadamente em SOURCE-BUILD-VALIDATION.md.
- Interface nativa: editor visual, compilação/preview com três códigos de barras e Tools > Options > iReport passam. Groovy e cm aparecem na interface; as cinco preferências sintéticas continuam no arquivo após encerramento normal.
- Reinicialização do mesmo perfil com Java 25: editor visual e recompilação/preview passam; log sem SEVERE em `selective-jdk-stax-native25.log`.
- Log nativo: sem SEVERE e sem erro Woodstox. Outros avisos de APIs antigas do NetBeans permanecem.
- NBM: 2626 classes idênticas às usadas no perfil nativo, bytecode máximo Java 17; 91 JARs auxiliares idênticos; PDFBox permanece somente nos testes.
- SHA-256 do NBM: `8eacb06ec7d49b816a7dd6a0d5fc6c255c9255e94d0054d1e30d948a4d994119`.

Evidências locais em `laboratorio/autonomous-2026-10-08`: `designer-jdk-stax-final21.log`, `designer-jdk-stax-final25.log`, `selective-jdk-stax-native.log`, `stax-nbm-audit.json` e `selective-jdk-stax-migration.json`. O NBM anterior foi preservado em `artifacts-production-candidate/ireport-designer-before-stax-fix.nbm`.

## Procedimento controlado

`Migrate-LabDesigner.py` aceita somente perfis dentro de `laboratorio`, com quarentena nova e separada. Consulta `update_tracking`, coloca em quarentena arquivos registrados como pertencentes ao designer antigo, components e JasperServer antigos, preserva arquivos compartilhados com outros módulos e extrai o novo NBM. Rejeita caminhos que escapam do perfil e sobrescritas de arquivos de terceiros antes de mover qualquer arquivo. Quatro testes verificam essas proteções.

Na cópia real do perfil legado, **174 arquivos foram preservados byte a byte**. Isso inclui os outros módulos e as preferências sintéticas. Os complementos antigos components/JasperServer foram colocados em quarentena: sua compatibilidade com a nova API não foi aprovada. Não se deve alterar a identidade do módulo apenas para forçar o carregamento desses complementos.

O procedimento é uma migração offline de laboratório; não equivale a uma atualização validada pelo assistente Plugins e não deve ser executado diretamente no perfil diário. Uma instalação em perfil limpo continua sendo a opção validada para o designer.

## Limites de liberação

Impressão na impressora afetada, dados reais, certificado de produção e complementos legados ainda precisam de homologação específica. Estes testes não certificam mudanças feitas posteriormente no ERP original nem permitem declarar aprovação integral de produção.
