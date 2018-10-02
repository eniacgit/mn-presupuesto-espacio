package org.camunda.bpm.menini_nicola.mn_proceso_espacio;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class TestReporteJasper {

	public static void main(String[] args) {
		// Creación de presupuesto espacio
		HashMap parametros = new HashMap<String, Object>();
		parametros.put("cotizacion", "180920-01");
		parametros.put("cliente", "Maria Valenzuela");
		parametros.put("email", "mariavelenzuela@gmail.com");
		parametros.put("descripcion", "bla bla bla");
		parametros.put("moneda", "US$");
		float costo = 150;
		parametros.put("costo", costo);
		parametros.put("condiciones", "30 dias");
		
		FileInputStream fis;
		try {
			fis = new FileInputStream("reportes//jasper//presupuestoEspacio.jasper");
			BufferedInputStream bufferedInputStream = new BufferedInputStream(fis);
			
			//Load bufferedInputStream file.jasper 
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(bufferedInputStream); 
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parametros,new JREmptyDataSource());
			JasperExportManager.exportReportToPdfFile(jasperPrint,"reportes//pdfs//presupuestoEspacio.pdf");
			JasperViewer.viewReport(jasperPrint, true);
		} catch (FileNotFoundException | JRException e) {
			e.printStackTrace();
		} 
		
	}

}
