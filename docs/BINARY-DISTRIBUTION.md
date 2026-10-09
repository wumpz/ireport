# Distribuição compilada do designer

O arquivo `ireport-designer-jr6.21.4.nbm` inclui 91 JARs de dependências do designer, incluindo os binários legados necessários provenientes do `libraries.zip` do upstream. A instalação não usa Maven nem baixa essas bibliotecas. Elas permanecem separadas dentro do módulo para preservar recursos, serviços e avisos de licença.

## Instalação

1. Use Apache NetBeans 31 com JDK 21 ou 25, configurações testadas. O módulo tem bytecode Java 17; isso, isoladamente, não certifica outras versões do IDE.
2. Faça backup do perfil. Remova a versão anterior do designer e desative complementos legados dependentes dela. Não sobreponha os antigos JARs de extensões: podem causar conflitos de classes e provedores XML.
3. Abra `Tools > Plugins > Downloaded > Add Plugins`, selecione o NBM e conclua a instalação. Reinicie o IDE.
4. Confirme `Tools > Options > iReport`, abra um JRXML de teste, compile e use Preview.
5. Relatórios que referenciem classes da aplicação precisam dessas classes no Classpath do iReport. Drivers de banco adicionais também são específicos do ambiente.

O ZIP offline contém o mesmo NBM, inventário SHA-256 das dependências, avisos extraídos sem alterações e esta documentação. Confira o download com `Get-FileHash -Algorithm SHA256` e `SHA256SUMS.txt`.

## Escopo e validação

Designer JasperReports 6.21.4, editor visual, conversão, Options e Preview foram testados em perfis isolados. O pacote auditado coincide com 2.626 classes e 91 bibliotecas do perfil nativo validado. Os 12 testes automatizados passam em JDK 21 e 25. O hash do NBM é `8eacb06ec7d49b816a7dd6a0d5fc6c255c9255e94d0054d1e30d948a4d994119`.

Esta é uma pré-release do designer. Não inclui a aplicação standalone completa nem certifica módulos antigos de JasperServer, Hadoop, MongoDB ou demais complementos. Não é uma atualização do runtime do ERP: os JARs atuais do ERP têm bloqueios próprios documentados em CURRENT-DELIVERY-VALIDATION.md.

## Código-fonte e bibliotecas históricas

O código correspondente está na tag da Release e no arquivo de fontes anexo. Para recompilar, siga Build-Designer.ps1 e Prepare-LegacyDependencies.ps1; o repositório mantém libraries.zip. A distribuição binária não é um espelho Maven completo nem garante uma recompilação offline em uma máquina sem cache Maven.

O POM declara AGPL-3.0 para o projeto. As dependências mantêm suas licenças próprias. O inventário identifica avisos embutidos disponíveis, incluindo ausências nos binários históricos; não equivale a uma auditoria jurídica completa.
