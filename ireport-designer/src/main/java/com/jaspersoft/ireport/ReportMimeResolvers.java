package com.jaspersoft.ireport;

import org.openide.filesystems.MIMEResolver;

/** Build-time MIME registrations required by current NetBeans. */
public final class ReportMimeResolvers {
    private ReportMimeResolvers() { }

    @MIMEResolver.Registration(displayName="JRXML report", resource="JrxmlResolver.xml", position=-100)
    public static void jrxml() { }

    @MIMEResolver.Registration(displayName="Compiled Jasper report", resource="JasperResolver.xml", position=3)
    public static void jasper() { }

    @MIMEResolver.Registration(displayName="Jasper style template", resource="JRTXResolver.xml", position=2)
    public static void jrtx() { }

    @MIMEResolver.Registration(displayName="Jasper chart theme", resource="JRCTXResolver.xml", position=1)
    public static void jrctx() { }

    @MIMEResolver.Registration(displayName="Report palette item", resource="PaletteItemResolver.xml", position=-200)
    public static void palette() { }

    @MIMEResolver.Registration(displayName="Report expression", resource="designer/editor/jrxml-expression.xml", position=-250)
    public static void expression() { }
}
