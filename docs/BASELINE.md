# Base de desenvolvimento

Upstream: https://github.com/wumpz/ireport

Commit de origem: `1ab6f2427cee2608722da2dccf34ee099cb7e98d`.

O módulo designer original compila e gera NBM sob Java 17.0.15/Maven 3.9.9,
com plataforma RELEASE160 e JasperReports 5.6.0. Execução registrada em
06/10/2026, em laboratório separado do Athene. Não houve teste visual desta
base no momento deste registro; o sucesso do build não certifica instalação.

Preparação:

```powershell
./scripts/Prepare-LegacyDependencies.ps1 -JavaHome 'C:/Program Files/Java/jdk-17' -WindowsTrust
./scripts/Build-Designer.ps1 -JavaHome 'C:/Program Files/Java/jdk-17' -WindowsTrust
```

`WindowsTrust` usa certificados confiados pelo Windows somente no processo
Maven. Não desativa TLS nem altera o truststore do Java instalado.
`build-settings.xml` redireciona os dois repositórios HTTP antigos para o
repositório HTTPS Jaspersoft. As bibliotecas não publicadas no Maven Central
provêm do `libraries.zip` versionado pelo upstream; o script registra seus
hashes. O artefato chart-themes tem versão interna 5.6.0-SNAPSHOT, limitação
herdada que deverá ser eliminada na atualização do motor.

O plugin de uso diário, fontes e compilados do ERP não devem ser modificados.
Instalar candidatos somente em userdir/cachedir próprios do laboratório.
