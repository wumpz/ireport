package com.jaspersoft.ireport.designer.compiler.prompt;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFormattedTextField;
import javax.swing.UIManager;
import net.sf.jasperreports.types.date.DateRangeBuilder;




/**
 *
 * @author gtoffoli
 */


public class DateRangeFormatter extends JFormattedTextField.AbstractFormatter {
        
          private static final Logger logger = Logger.getLogger(DateRangeFormatter.class.getName());
    
        private DateFormat _formats[] = null;
        private int _formatIndex = 0;
        private String dateRangeExpression = null;

        public DateRangeFormatter() {
            _formats = new DateFormat[3];
            String format = UIManager.getString("JXDatePicker.longFormat");
            if (format == null) {
                format = "EEE MM/dd/yyyy";
            }
            _formats[0] = new SimpleDateFormat(format);

            format = UIManager.getString("JXDatePicker.mediumFormat");
            if (format == null) {
                format = "MM/dd/yyyy";
            }
            _formats[1] = new SimpleDateFormat(format);

            format = UIManager.getString("JXDatePicker.shortFormat");
            if (format == null) {
                format = "MM/dd";
            }
            _formats[2] = new SimpleDateFormat(format);
        }

        public DateRangeFormatter(DateFormat formats[]) {
            _formats = formats;
        }

        public DateFormat[] getFormats() {
            return _formats;
        }

        /**
         * {@inheritDoc}
         */
        public Object stringToValue(String text) throws ParseException {
            
            
            final String s = text;
        
            Object result = null;
            ParseException pex = null;

            if (text == null || text.trim().length() == 0) {
                return null;
            }

            // If the current formatter did not work loop through the other
            // formatters and see if any of them can parse the string passed
            // in.
            for (int i = 0; i < _formats.length; i++) {
                try {
                    result = (_formats[i]).parse(text);

                    // We got a successful formatter.  Update the
                    // current formatter index.
                    _formatIndex = i;
                    dateRangeExpression = null;
                    pex = null;
                    break;
                } catch (ParseException ex) {
                    pex = ex;
                }
            }

            
            // Try to check if this is a valid date range expression...
            if (pex != null) {
                try {
                    
                    text = text.replaceAll(" ","");
                    
                    DateRangeBuilder b = new DateRangeBuilder(text);
                    Date dt = b.toDateRange().getStart();
                    
                    dateRangeExpression = text;
                    pex = null;
                    return text;
                    
                } catch (Exception ex)
                {
                }
            }
                
            
            if (pex != null) {     
                throw pex;
            }
            
            return result;
        }

        /**
         * {@inheritDoc}
         */
        public String valueToString(Object value) throws ParseException {
            
            
            //logger.log(Level.WARNING, "valueToString " + value);
           
            
            if (value != null) {
                 
                if (value instanceof String)
                {
                    return (String)value;
                }
                 
                return _formats[_formatIndex].format(value);
            }
              
             
            return null;
             
            
        }
    }