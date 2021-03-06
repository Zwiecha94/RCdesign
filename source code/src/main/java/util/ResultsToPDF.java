package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import SLS.Sls;
import diagnosis.DiagnosisMainAlgorithm;
import mainalgorithm.InternalForces;
import mainalgorithm.Reinforcement;
import materials.Concrete;
import materials.DimensionsOfCrossSectionOfConcrete;
import materials.Steel;

public class ResultsToPDF {

	/** Path to the resulting PDF file. */

	private static String GetExecutionPathDesign() {
		ifPdfNameIsEmptyReturnNewName();
		String absolutePath = CompyPdf.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf("/"));
		absolutePath = absolutePath.replaceAll("%20", " "); // Surely need to do
		// this here
		absolutePath = absolutePath + "/" + pdfName + "_p.pdf";
		return absolutePath;
	}

	private static String GetExecutionPathDiagnosis() {
		ifPdfNameIsEmptyReturnNewName();
		String absolutePath = CompyPdf.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf("/"));
		absolutePath = absolutePath.replaceAll("%20", " "); // Surely need to do
		// this here
		absolutePath = absolutePath + "/" + pdfName + "_d.pdf";
		return absolutePath;
	}

	private static String GetExecutionPathForSecondFile() {
		ifPdfNameIsEmptyReturnNewName();
		String absolutePath = CompyPdf.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf("/"));
		absolutePath = absolutePath.replaceAll("%20", " "); // Surely need to do
		// this here
		absolutePath = absolutePath + "/" + pdfName + "WynikiPosrednie.pdf";
		return absolutePath;
	}

	private static String DEST = "C:/Users/Mieciac/Desktop/testresult.pdf";
	private static String pdfName = "Wyniki";

	public static void setPdfName(String name) {
		pdfName = name;
	}

	private static void ifPdfNameIsEmptyReturnNewName() {
		if (pdfName.equals("")) {
			pdfName = "Wyniki";
		}
	}

	public static void saveDesingResultsToPDF(Concrete concrete, Steel steel, Reinforcement reinforcement, InternalForces forces, DimensionsOfCrossSectionOfConcrete dimensions, Sls sls)
			throws IOException, DocumentException {
		if (dimensions.getisBeamRectangular()) {
			if (forces.getnEd() == 0) {
				saveRectangularBeamReinforcement(concrete, steel, reinforcement, forces, dimensions, sls);
			} else {
				saveBeamAxisLoadResulsts(concrete, steel, reinforcement, forces, dimensions, sls);
			}
		} else {
			saveTShapedBeamResulsts(concrete, steel, reinforcement, forces, dimensions, sls);
		}
		// printResultsForJacekIntoSecondFile();
	}

	public static void saveRectangularBeamReinforcement(Concrete concrete, Steel steel, Reinforcement reinforcement, InternalForces forces, DimensionsOfCrossSectionOfConcrete dimensions, Sls sls)
			throws IOException, DocumentException {
		String SRC = "plates/RectangularBeam.pdf";
		File file = new File(SRC);
		file.getParentFile().mkdirs();
		printRectangularBeamResults(SRC, GetExecutionPathDesign(), concrete, steel, reinforcement, forces, dimensions, sls);
	}

	public static void printRectangularBeamResults(String src, String dest, Concrete concrete, Steel steel, Reinforcement reinforcement, InternalForces forces,
			DimensionsOfCrossSectionOfConcrete dimensions, Sls sls) throws IOException, DocumentException {
		Document document = new Document(PageSize.A4);
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
		document.open();
		PdfContentByte cb = writer.getDirectContent();
		PdfReader reader = new PdfReader(src);
		PdfImportedPage page = writer.getImportedPage(reader, 1);
		document.newPage();
		cb.addTemplate(page, 0, 0);
		writer.setCompressionLevel(0);

		Paragraph paragraph1 = new Paragraph("");
		Paragraph paragraph2 = new Paragraph("           Nazwa zadania: " + pdfName);
		paragraph2.setAlignment(Element.ALIGN_LEFT);

		document.add(paragraph1);
		document.add(paragraph2);

		// forces

		cb.beginText();
		cb.moveText(296, 665);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(OutputFormatter.diagnosisMed(forces.getmEd()));
		cb.endText();

		cb.beginText();
		cb.moveText(310, 653);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(OutputFormatter.diagnosisMed(forces.getCharacteristicMEdCalkowita()));
		cb.endText();

		cb.beginText();
		cb.moveText(310, 640);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(OutputFormatter.diagnosisMed(forces.getCharacteristicMEdDlugotrwale()));
		cb.endText();

		cb.beginText();
		cb.moveText(296, 628);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(OutputFormatter.diagnosisVedAndNed(forces.getvEd()));
		cb.endText();

		// concrete and steel
		cb.beginText();
		cb.moveText(110, 666);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(concrete.getConcreteClass());
		cb.endText();

		cb.beginText();
		cb.moveText(124, 652);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(Integer.toString(steel.getFYk()));
		cb.endText();
		// geometry parameters
		cb.beginText();
		cb.moveText(110, 611);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.dimensionsPrintformatter(dimensions.getB()));
		cb.endText();

		cb.beginText();
		cb.moveText(110, 598);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.a1a2printformatter(dimensions.getA1()));
		cb.endText();

		cb.beginText();
		cb.moveText(110, 584);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.a1a2printformatter(dimensions.getlEff()));
		cb.endText();

		cb.beginText();
		cb.moveText(200, 611);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.dimensionsPrintformatter(dimensions.getH()));
		cb.endText();

		cb.beginText();
		cb.moveText(200, 598);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.a1a2printformatter(dimensions.getA2()));
		cb.endText();

		// as1

		cb.beginText();
		cb.moveText(160, 468);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.as1as2PrintFormatter(reinforcement.getRequiredSymmetricalAS1()));
		cb.endText();

		cb.beginText();
		cb.moveText(345, 468);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(PrintFormatter.as1as2PrintFormatter(reinforcement.getDesignedSymmetricalAS1()));
		cb.endText();

		cb.beginText();
		cb.moveText(470, 468);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(Integer.toString(reinforcement.getRequiredNumberOfSymmetricalRodsAS1()));
		cb.endText();

		cb.beginText();
		cb.moveText(495, 468);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getDesignedDiameterSymmetricalAS1()));
		cb.endText();
		// as2
		cb.beginText();
		cb.moveText(160, 448);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.as1as2PrintFormatter(reinforcement.getRequiredSymmetricalAS2()));
		cb.endText();

		cb.beginText();
		cb.moveText(345, 448);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(PrintFormatter.as1as2PrintFormatter(reinforcement.getDesignedSymmetricalAS2()));
		cb.endText();

		cb.beginText();
		cb.moveText(470, 448);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(Integer.toString(reinforcement.getRequiredNumberOfSymmetricalRodsAS2()));
		cb.endText();

		cb.beginText();
		cb.moveText(495, 448);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getDesignedDiameterSymmetricalAS2()));
		cb.endText();

		// vertical sitruups

		cb.beginText();
		cb.moveText(255, 405);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getaSW1Diameter()));
		cb.endText();

		cb.beginText();
		cb.moveText(382, 405);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getnS1()));
		cb.endText();

		cb.beginText();
		cb.moveText(450, 405);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(PrintFormatter.s1s2(reinforcement.getS1Required()));
		cb.endText();

		// bent rods
		cb.beginText();
		cb.moveText(255, 378);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getaSW2Diameter()));
		cb.endText();

		cb.beginText();
		cb.moveText(382, 378);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getnS2Designed()));
		cb.endText();

		cb.beginText();
		cb.moveText(450, 378);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(PrintFormatter.s1s2(reinforcement.getS2Required()));
		cb.endText();

		// sls

		cb.beginText();
		cb.moveText(175, 330);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(PrintFormatter.wFormatter(sls.getwSymmetricalDesigned()));
		cb.endText();

		cb.beginText();
		cb.moveText(330, 330);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(PrintFormatter.fFormatter(sls.getfSymmetricalDesigned()));
		cb.endText();

		document.close();
	}

	public static void saveTShapedBeamResulsts(Concrete concrete, Steel steel, Reinforcement reinforcement, InternalForces forces, DimensionsOfCrossSectionOfConcrete dimensions, Sls sls)
			throws IOException, DocumentException {
		String SRC = "plates/TShapedBeam.pdf";
		File file = new File(SRC);
		file.getParentFile().mkdirs();
		printTShapedBeamResults(SRC, GetExecutionPathDesign(), concrete, steel, reinforcement, forces, dimensions, sls);
	}

	private static void printTShapedBeamResults(String src, String dest, Concrete concrete, Steel steel, Reinforcement reinforcement, InternalForces forces,
			DimensionsOfCrossSectionOfConcrete dimensions, Sls sls) throws IOException, DocumentException {

		Document document = new Document(PageSize.A4);
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
		document.open();
		PdfContentByte cb = writer.getDirectContent();
		PdfReader reader = new PdfReader(src);
		PdfImportedPage page = writer.getImportedPage(reader, 1);
		document.newPage();
		cb.addTemplate(page, 0, 0);
		writer.setCompressionLevel(0);

		Paragraph paragraph1 = new Paragraph("");
		Paragraph paragraph2 = new Paragraph("           Nazwa zadania: " + pdfName);
		paragraph2.setAlignment(Element.ALIGN_LEFT);

		document.add(paragraph1);
		document.add(paragraph2);
		// forces

		cb.beginText();
		cb.moveText(296, 665);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(OutputFormatter.diagnosisMed(forces.getmEd()));
		cb.endText();

		cb.beginText();
		cb.moveText(310, 653);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(OutputFormatter.diagnosisMed(forces.getCharacteristicMEdCalkowita()));
		cb.endText();

		cb.beginText();
		cb.moveText(310, 640);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(OutputFormatter.diagnosisMed(forces.getCharacteristicMEdDlugotrwale()));
		cb.endText();

		cb.beginText();
		cb.moveText(296, 628);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(OutputFormatter.diagnosisVedAndNed(forces.getvEd()));
		cb.endText();

		// concrete and steel
		cb.beginText();
		cb.moveText(110, 666);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(concrete.getConcreteClass());
		cb.endText();

		cb.beginText();
		cb.moveText(124, 652);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(Integer.toString(steel.getFYk()));
		cb.endText();
		// geometry parameters
		cb.beginText();
		cb.moveText(110, 612);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.dimensionsPrintformatter(dimensions.getB()));
		cb.endText();

		cb.beginText();
		cb.moveText(110, 599);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.dimensionsPrintformatter(dimensions.getH()));
		cb.endText();

		cb.beginText();
		cb.moveText(110, 585);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.a1a2printformatter(dimensions.getA1()));
		cb.endText();

		cb.beginText();
		cb.moveText(200, 612);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText((PrintFormatter.a1a2printformatter(dimensions.getbEff())));
		cb.endText();

		cb.beginText();
		cb.moveText(200, 599);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.a1a2printformatter(dimensions.gettW()));
		cb.endText();

		cb.beginText();
		cb.moveText(110, 571);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.a1a2printformatter(dimensions.getlEff()));
		cb.endText();

		cb.beginText();
		cb.moveText(200, 585);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.a1a2printformatter(dimensions.getlEff()));
		cb.endText();

		// as1

		cb.beginText();
		cb.moveText(160, 464);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.as1as2PrintFormatter(reinforcement.getRequiredSymmetricalAS1()));
		cb.endText();

		cb.beginText();
		cb.moveText(345, 464);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(PrintFormatter.as1as2PrintFormatter(reinforcement.getDesignedSymmetricalAS1()));
		cb.endText();

		cb.beginText();
		cb.moveText(470, 464);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(Integer.toString(reinforcement.getRequiredNumberOfSymmetricalRodsAS1()));
		cb.endText();

		cb.beginText();
		cb.moveText(495, 464);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getDesignedDiameterSymmetricalAS1()));
		cb.endText();
		// as2
		cb.beginText();
		cb.moveText(160, 444);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.as1as2PrintFormatter(reinforcement.getRequiredSymmetricalAS2()));
		cb.endText();

		cb.beginText();
		cb.moveText(345, 444);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(PrintFormatter.as1as2PrintFormatter(reinforcement.getDesignedSymmetricalAS2()));
		cb.endText();

		cb.beginText();
		cb.moveText(470, 444);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(Integer.toString(reinforcement.getRequiredNumberOfSymmetricalRodsAS2()));
		cb.endText();

		cb.beginText();
		cb.moveText(495, 444);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getDesignedDiameterSymmetricalAS2()));
		cb.endText();

		// vertical sitruups

		cb.beginText();
		cb.moveText(255, 401);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getaSW1Diameter()));
		cb.endText();

		cb.beginText();
		cb.moveText(382, 401);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getnS1()));
		cb.endText();

		cb.beginText();
		cb.moveText(450, 401);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(PrintFormatter.s1s2(reinforcement.getS1Required()));
		cb.endText();

		// bent rods
		cb.beginText();
		cb.moveText(255, 374);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getaSW2Diameter()));
		cb.endText();

		cb.beginText();
		cb.moveText(382, 374);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getnS2Designed()));
		cb.endText();

		cb.beginText();
		cb.moveText(450, 374);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(PrintFormatter.s1s2(reinforcement.getS2Required()));
		cb.endText();

		// sls

		cb.beginText();
		cb.moveText(175, 326);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(PrintFormatter.wFormatter(sls.getwSymmetricalDesigned()));
		cb.endText();

		cb.beginText();
		cb.moveText(330, 326);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(PrintFormatter.fFormatter(sls.getfSymmetricalDesigned()));
		cb.endText();
		//
		document.close();
	}

	public static void saveBeamAxisLoadResulsts(Concrete concrete, Steel steel, Reinforcement reinforcement, InternalForces forces, DimensionsOfCrossSectionOfConcrete dimensions, Sls sls)
			throws IOException, DocumentException {
		String SRC = "plates/BeamAxisLoad.pdf";
		File file = new File(SRC);
		file.getParentFile().mkdirs();
		printBeamAxisLoadBeamResults(SRC, GetExecutionPathDesign(), concrete, steel, reinforcement, forces, dimensions, sls);
	}

	private static void printBeamAxisLoadBeamResults(String src, String dest, Concrete concrete, Steel steel, Reinforcement reinforcement, InternalForces forces,
			DimensionsOfCrossSectionOfConcrete dimensions, Sls sls) throws IOException, DocumentException {

		Document document = new Document(PageSize.A4);
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
		document.open();
		PdfContentByte cb = writer.getDirectContent();
		PdfReader reader = new PdfReader(src);
		PdfImportedPage page = writer.getImportedPage(reader, 1);
		document.newPage();
		cb.addTemplate(page, 0, 0);
		writer.setCompressionLevel(0);

		Paragraph paragraph1 = new Paragraph("");
		Paragraph paragraph2 = new Paragraph("           Nazwa zadania: " + pdfName);
		paragraph2.setAlignment(Element.ALIGN_LEFT);

		document.add(paragraph1);
		document.add(paragraph2);
		// forces

		cb.beginText();
		cb.moveText(296, 665);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(OutputFormatter.diagnosisMed(forces.getmEd()));
		cb.endText();

		cb.beginText();
		cb.moveText(296, 653);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(OutputFormatter.diagnosisVedAndNed(forces.getnEd()));
		cb.endText();

		cb.beginText();
		cb.moveText(296, 640);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(OutputFormatter.diagnosisVedAndNed(forces.getvEd()));
		cb.endText();

		// concrete and steel
		cb.beginText();
		cb.moveText(110, 666);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(concrete.getConcreteClass());
		cb.endText();

		cb.beginText();
		cb.moveText(124, 652);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(Integer.toString(steel.getFYk()));
		cb.endText();
		// geometry parameters

		cb.beginText();
		cb.moveText(110, 611);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.dimensionsPrintformatter(dimensions.getB()));
		cb.endText();

		cb.beginText();
		cb.moveText(110, 598);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.a1a2printformatter(dimensions.getA1()));
		cb.endText();

		cb.beginText();
		cb.moveText(110, 584);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.a1a2printformatter(dimensions.getlEff()));
		cb.endText();

		cb.beginText();
		cb.moveText(200, 611);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.dimensionsPrintformatter(dimensions.getH()));
		cb.endText();

		cb.beginText();
		cb.moveText(200, 598);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.a1a2printformatter(dimensions.getA2()));
		cb.endText();

		// as1

		cb.beginText();
		cb.moveText(160, 476);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.as1as2PrintFormatter(reinforcement.getRequiredSymmetricalAS1()));
		cb.endText();

		cb.beginText();
		cb.moveText(345, 476);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(PrintFormatter.as1as2PrintFormatter(reinforcement.getDesignedSymmetricalAS1()));
		cb.endText();

		cb.beginText();
		cb.moveText(470, 476);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(Integer.toString(reinforcement.getRequiredNumberOfSymmetricalRodsAS1()));
		cb.endText();

		cb.beginText();
		cb.moveText(495, 476);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getDesignedDiameterSymmetricalAS1()));
		cb.endText();
		// as2
		cb.beginText();
		cb.moveText(160, 456);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.as1as2PrintFormatter(reinforcement.getRequiredSymmetricalAS2()));
		cb.endText();

		cb.beginText();
		cb.moveText(345, 456);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(PrintFormatter.as1as2PrintFormatter(reinforcement.getDesignedSymmetricalAS2()));
		cb.endText();

		cb.beginText();
		cb.moveText(470, 456);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(Integer.toString(reinforcement.getRequiredNumberOfSymmetricalRodsAS2()));
		cb.endText();

		cb.beginText();
		cb.moveText(495, 456);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getDesignedDiameterSymmetricalAS2()));
		cb.endText();

		// as11

		cb.beginText();
		cb.moveText(160, 408);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.as1as2PrintFormatter(reinforcement.getRequiredUnsymmetricalAS1()));
		cb.endText();

		cb.beginText();
		cb.moveText(345, 408);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(PrintFormatter.as1as2PrintFormatter(reinforcement.getDesingedUnsymmetricalAS1()));
		cb.endText();

		cb.beginText();
		cb.moveText(470, 408);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(Integer.toString(reinforcement.getRequiredNumberOfUnsymmetricalRodsAS1()));
		cb.endText();

		cb.beginText();
		cb.moveText(495, 408);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getDesignedDiameterSymmetricalAS1()));
		cb.endText();
		// as22
		cb.beginText();
		cb.moveText(160, 388);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.as1as2PrintFormatter(reinforcement.getRequiredUnsymmetricalAS2()));
		cb.endText();

		cb.beginText();
		cb.moveText(345, 388);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(PrintFormatter.as1as2PrintFormatter(reinforcement.getDesignedUnsymmetricalAS2()));
		cb.endText();

		cb.beginText();
		cb.moveText(470, 388);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(Integer.toString(reinforcement.getRequiredNumberOfUnsymmetricalRodsAS2()));
		cb.endText();

		cb.beginText();
		cb.moveText(495, 388);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getDesignedDiameterSymmetricalAS2()));
		cb.endText();
		// vertical sitruups

		cb.beginText();
		cb.moveText(255, 345);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getaSW1Diameter()));
		cb.endText();

		cb.beginText();
		cb.moveText(382, 345);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getnS1()));
		cb.endText();

		cb.beginText();
		cb.moveText(450, 345);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(PrintFormatter.s1s2(reinforcement.getS1Required()));
		cb.endText();

		// bent rods
		cb.beginText();
		cb.moveText(255, 318);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getaSW2Diameter()));
		cb.endText();

		cb.beginText();
		cb.moveText(382, 318);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getnS2Designed()));
		cb.endText();

		cb.beginText();
		cb.moveText(450, 318);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(PrintFormatter.s1s2(reinforcement.getS2Required()));
		cb.endText();

		//
		document.close();
	}

	public static void saveDiagnosisResultsToPDF(Concrete concrete, Steel steel, Reinforcement reinforcement, InternalForces forces, DimensionsOfCrossSectionOfConcrete dimensions, Sls sls,
			DiagnosisMainAlgorithm diagnosis) throws IOException, DocumentException {
		if (dimensions.getisBeamRectangular()) {
			if (forces.getnEd() == 0) {
				saveRectangularBeamDiagnosis(concrete, steel, reinforcement, forces, dimensions, sls, diagnosis);
			} else {
				saveAxisLoadBeamDiagnosis(concrete, steel, reinforcement, forces, dimensions, sls, diagnosis);
			}
		} else {
			saveRectangularTShapedBeamDiagnosis(concrete, steel, reinforcement, forces, dimensions, sls, diagnosis);
		}
	}

	public static void saveRectangularBeamDiagnosis(Concrete concrete, Steel steel, Reinforcement reinforcement, InternalForces forces, DimensionsOfCrossSectionOfConcrete dimensions, Sls sls,
			DiagnosisMainAlgorithm diagnosis) throws IOException, DocumentException {
		String SRC = "plates/BeamBendingDiagnosis.pdf";
		File file = new File(SRC);
		file.getParentFile().mkdirs();
		printRectangularBeamResultsDiagnosis(SRC, GetExecutionPathDiagnosis(), concrete, steel, reinforcement, forces, dimensions, sls, diagnosis);
	}

	public static void printRectangularBeamResultsDiagnosis(String src, String dest, Concrete concrete, Steel steel, Reinforcement reinforcement, InternalForces forces,
			DimensionsOfCrossSectionOfConcrete dimensions, Sls sls, DiagnosisMainAlgorithm diagnosis) throws IOException, DocumentException {
		Document document = new Document(PageSize.A4);
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
		document.open();
		PdfContentByte cb = writer.getDirectContent();
		PdfReader reader = new PdfReader(src);
		PdfImportedPage page = writer.getImportedPage(reader, 1);
		document.newPage();
		cb.addTemplate(page, 0, 0);
		writer.setCompressionLevel(0);

		Paragraph paragraph1 = new Paragraph("");
		Paragraph paragraph2 = new Paragraph("           Nazwa zadania: " + pdfName);
		paragraph2.setAlignment(Element.ALIGN_LEFT);

		document.add(paragraph1);
		document.add(paragraph2);
		// forces

		cb.beginText();
		cb.moveText(110, 500);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(OutputFormatter.diagnosisMed(diagnosis.getmRdDesignedSymmetrical()));
		cb.endText();

		cb.beginText();
		cb.moveText(110, 483);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(OutputFormatter.diagnosisVedAndNed(diagnosis.getvRdDesigned()));
		cb.endText();

		// concrete and steel
		cb.beginText();
		cb.moveText(110, 666);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(concrete.getConcreteClass());
		cb.endText();

		cb.beginText();
		cb.moveText(124, 652);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(Integer.toString(steel.getFYk()));
		cb.endText();
		// geometry parameters
		cb.beginText();
		cb.moveText(110, 611);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.dimensionsPrintformatter(dimensions.getB()));
		cb.endText();

		cb.beginText();
		cb.moveText(110, 598);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.a1a2printformatter(dimensions.getA1()));
		cb.endText();

		cb.beginText();
		cb.moveText(110, 584);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.a1a2printformatter(dimensions.getlEff()));
		cb.endText();

		cb.beginText();
		cb.moveText(200, 611);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.dimensionsPrintformatter(dimensions.getH()));
		cb.endText();

		cb.beginText();
		cb.moveText(200, 598);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.a1a2printformatter(dimensions.getA2()));
		cb.endText();

		// as1

		cb.beginText();
		cb.moveText(290, 666);
		cb.setFontAndSize(BaseFont.createFont(), 10);
		cb.showText(PrintFormatter.as1as2PrintFormatter(reinforcement.getDesignedSymmetricalAS1()));
		cb.endText();

		cb.beginText();
		cb.moveText(346, 665);
		cb.setFontAndSize(BaseFont.createFont(), 10);
		cb.showText(Integer.toString(reinforcement.getRequiredNumberOfSymmetricalRodsAS1()));
		cb.endText();

		cb.beginText();
		cb.moveText(365, 665);
		cb.setFontAndSize(BaseFont.createFont(), 10);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getDesignedDiameterSymmetricalAS1()));
		cb.endText();

		// as2
		cb.beginText();
		cb.moveText(290, 653);
		cb.setFontAndSize(BaseFont.createFont(), 10);
		cb.showText(PrintFormatter.as1as2PrintFormatter(reinforcement.getDesignedSymmetricalAS2()));
		cb.endText();

		cb.beginText();
		cb.moveText(346, 652);
		cb.setFontAndSize(BaseFont.createFont(), 10);
		cb.showText(Integer.toString(reinforcement.getRequiredNumberOfSymmetricalRodsAS2()));
		cb.endText();

		cb.beginText();
		cb.moveText(365, 652);
		cb.setFontAndSize(BaseFont.createFont(), 10);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getDesignedDiameterSymmetricalAS2()));
		cb.endText();

		// vertical sitruups

		cb.beginText();
		cb.moveText(365, 627);
		cb.setFontAndSize(BaseFont.createFont(), 10);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getaSW1Diameter()));
		cb.endText();

		cb.beginText();
		cb.moveText(292, 613);
		cb.setFontAndSize(BaseFont.createFont(), 10);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getnS1()));
		cb.endText();

		cb.beginText();
		cb.moveText(330, 613);
		cb.setFontAndSize(BaseFont.createFont(), 10);
		cb.showText(PrintFormatter.s1s2(reinforcement.getS1Required()));
		cb.endText();

		// bent rods
		cb.beginText();
		cb.moveText(365, 599);
		cb.setFontAndSize(BaseFont.createFont(), 10);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getaSW2Diameter()));
		cb.endText();

		cb.beginText();
		cb.moveText(292, 585);
		cb.setFontAndSize(BaseFont.createFont(), 10);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getnS2Designed()));
		cb.endText();

		cb.beginText();
		cb.moveText(330, 585);
		cb.setFontAndSize(BaseFont.createFont(), 10);
		cb.showText(PrintFormatter.s1s2(reinforcement.getS2Required()));
		cb.endText();

		// sls

		document.close();
	}

	public static void saveRectangularTShapedBeamDiagnosis(Concrete concrete, Steel steel, Reinforcement reinforcement, InternalForces forces, DimensionsOfCrossSectionOfConcrete dimensions, Sls sls,
			DiagnosisMainAlgorithm diagnosis) throws IOException, DocumentException {
		String SRC = "plates/TBeamBendingDiagnosis.pdf";
		File file = new File(SRC);
		file.getParentFile().mkdirs();
		printRectangularTshapedBeamResultsDiagnosis(SRC, GetExecutionPathDiagnosis(), concrete, steel, reinforcement, forces, dimensions, sls, diagnosis);
	}

	public static void printRectangularTshapedBeamResultsDiagnosis(String src, String dest, Concrete concrete, Steel steel, Reinforcement reinforcement, InternalForces forces,
			DimensionsOfCrossSectionOfConcrete dimensions, Sls sls, DiagnosisMainAlgorithm diagnosis) throws IOException, DocumentException {
		Document document = new Document(PageSize.A4);
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
		document.open();
		PdfContentByte cb = writer.getDirectContent();
		PdfReader reader = new PdfReader(src);
		PdfImportedPage page = writer.getImportedPage(reader, 1);
		document.newPage();
		cb.addTemplate(page, 0, 0);
		writer.setCompressionLevel(0);

		Paragraph paragraph1 = new Paragraph("");
		Paragraph paragraph2 = new Paragraph("           Nazwa zadania: " + pdfName);
		paragraph2.setAlignment(Element.ALIGN_LEFT);

		document.add(paragraph1);
		document.add(paragraph2);
		// forces

		cb.beginText();
		cb.moveText(110, 499);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(OutputFormatter.diagnosisMed(diagnosis.getmRdDesignedSymmetrical()));
		cb.endText();

		cb.beginText();
		cb.moveText(110, 483);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(OutputFormatter.diagnosisVedAndNed(diagnosis.getvRdDesigned()));
		cb.endText();

		// concrete and steel
		cb.beginText();
		cb.moveText(110, 666);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(concrete.getConcreteClass());
		cb.endText();

		cb.beginText();
		cb.moveText(124, 652);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(Integer.toString(steel.getFYk()));
		cb.endText();
		// geometry parameters
		cb.beginText();
		cb.moveText(110, 611);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.dimensionsPrintformatter(dimensions.getB()));
		cb.endText();

		cb.beginText();
		cb.moveText(110, 598);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.dimensionsPrintformatter(dimensions.getH()));
		cb.endText();

		cb.beginText();
		cb.moveText(110, 584);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.a1a2printformatter(dimensions.getA1()));
		cb.endText();

		cb.beginText();
		cb.moveText(200, 611);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText((PrintFormatter.a1a2printformatter(dimensions.getbEff())));
		cb.endText();

		cb.beginText();
		cb.moveText(200, 598);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.a1a2printformatter(dimensions.gettW()));
		cb.endText();

		cb.beginText();
		cb.moveText(110, 570);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.a1a2printformatter(dimensions.getlEff()));
		cb.endText();

		cb.beginText();
		cb.moveText(200, 584);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.a1a2printformatter(dimensions.getlEff()));
		cb.endText();

		// as1

		cb.beginText();
		cb.moveText(290, 666);
		cb.setFontAndSize(BaseFont.createFont(), 10);
		cb.showText(PrintFormatter.as1as2PrintFormatter(reinforcement.getDesignedSymmetricalAS1()));
		cb.endText();

		cb.beginText();
		cb.moveText(346, 665);
		cb.setFontAndSize(BaseFont.createFont(), 10);
		cb.showText(Integer.toString(reinforcement.getRequiredNumberOfSymmetricalRodsAS1()));
		cb.endText();

		cb.beginText();
		cb.moveText(365, 665);
		cb.setFontAndSize(BaseFont.createFont(), 10);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getDesignedDiameterSymmetricalAS1()));
		cb.endText();

		// as2
		cb.beginText();
		cb.moveText(290, 653);
		cb.setFontAndSize(BaseFont.createFont(), 10);
		cb.showText(PrintFormatter.as1as2PrintFormatter(reinforcement.getDesignedSymmetricalAS2()));
		cb.endText();

		cb.beginText();
		cb.moveText(346, 652);
		cb.setFontAndSize(BaseFont.createFont(), 10);
		cb.showText(Integer.toString(reinforcement.getRequiredNumberOfSymmetricalRodsAS2()));
		cb.endText();

		cb.beginText();
		cb.moveText(365, 652);
		cb.setFontAndSize(BaseFont.createFont(), 10);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getDesignedDiameterSymmetricalAS2()));
		cb.endText();

		// vertical sitruups

		cb.beginText();
		cb.moveText(365, 627);
		cb.setFontAndSize(BaseFont.createFont(), 10);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getaSW1Diameter()));
		cb.endText();

		cb.beginText();
		cb.moveText(292, 613);
		cb.setFontAndSize(BaseFont.createFont(), 10);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getnS1()));
		cb.endText();

		cb.beginText();
		cb.moveText(330, 613);
		cb.setFontAndSize(BaseFont.createFont(), 10);
		cb.showText(PrintFormatter.s1s2(reinforcement.getS1Required()));
		cb.endText();

		// bent rods
		cb.beginText();
		cb.moveText(365, 599);
		cb.setFontAndSize(BaseFont.createFont(), 10);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getaSW2Diameter()));
		cb.endText();

		cb.beginText();
		cb.moveText(292, 585);
		cb.setFontAndSize(BaseFont.createFont(), 10);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getnS2Designed()));
		cb.endText();

		cb.beginText();
		cb.moveText(330, 585);
		cb.setFontAndSize(BaseFont.createFont(), 10);
		cb.showText(PrintFormatter.s1s2(reinforcement.getS2Required()));
		cb.endText();
		// sls

		document.close();
	}

	public static void saveAxisLoadBeamDiagnosis(Concrete concrete, Steel steel, Reinforcement reinforcement, InternalForces forces, DimensionsOfCrossSectionOfConcrete dimensions, Sls sls,
			DiagnosisMainAlgorithm diagnosis) throws IOException, DocumentException {
		String SRC = "plates/BeamAxisLoadDiagnosis.pdf";
		File file = new File(SRC);
		file.getParentFile().mkdirs();
		printAxisLoadResultsDiagnosis(SRC, GetExecutionPathDiagnosis(), concrete, steel, reinforcement, forces, dimensions, sls, diagnosis);
	}

	public static void printAxisLoadResultsDiagnosis(String src, String dest, Concrete concrete, Steel steel, Reinforcement reinforcement, InternalForces forces,
			DimensionsOfCrossSectionOfConcrete dimensions, Sls sls, DiagnosisMainAlgorithm diagnosis) throws IOException, DocumentException {
		Document document = new Document(PageSize.A4);
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
		document.open();
		PdfContentByte cb = writer.getDirectContent();
		PdfReader reader = new PdfReader(src);
		PdfImportedPage page = writer.getImportedPage(reader, 1);
		document.newPage();
		cb.addTemplate(page, 0, 0);
		writer.setCompressionLevel(0);

		Paragraph paragraph1 = new Paragraph("");
		Paragraph paragraph2 = new Paragraph("           Nazwa zadania: " + pdfName);
		paragraph2.setAlignment(Element.ALIGN_LEFT);

		document.add(paragraph1);
		document.add(paragraph2);
		// forces

		cb.beginText();
		cb.moveText(110, 500);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(OutputFormatter.diagnosisMed(diagnosis.getmRdDesignedSymmetrical()));
		cb.endText();

		cb.beginText();
		cb.moveText(110, 483);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(OutputFormatter.diagnosisVedAndNed(diagnosis.getnRdDesignedSymmetrical()));
		cb.endText();

		cb.beginText();
		cb.moveText(110, 467);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(OutputFormatter.diagnosisVedAndNed(diagnosis.getvRdDesigned()));
		cb.endText();

		// concrete and steel
		cb.beginText();
		cb.moveText(110, 666);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(concrete.getConcreteClass());
		cb.endText();

		cb.beginText();
		cb.moveText(124, 652);
		cb.setFontAndSize(BaseFont.createFont(), 12);
		cb.showText(Integer.toString(steel.getFYk()));
		cb.endText();
		// geometry parameters
		cb.beginText();
		cb.moveText(110, 611);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.dimensionsPrintformatter(dimensions.getB()));
		cb.endText();

		cb.beginText();
		cb.moveText(110, 598);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.a1a2printformatter(dimensions.getA1()));
		cb.endText();

		cb.beginText();
		cb.moveText(110, 584);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.a1a2printformatter(dimensions.getlEff()));
		cb.endText();

		cb.beginText();
		cb.moveText(200, 611);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.dimensionsPrintformatter(dimensions.getH()));
		cb.endText();

		cb.beginText();
		cb.moveText(200, 598);
		cb.setFontAndSize(BaseFont.createFont(), 11);
		cb.showText(PrintFormatter.a1a2printformatter(dimensions.getA2()));
		cb.endText();
		// as1

		cb.beginText();
		cb.moveText(290, 666);
		cb.setFontAndSize(BaseFont.createFont(), 10);
		cb.showText(PrintFormatter.as1as2PrintFormatter(reinforcement.getDesignedSymmetricalAS1()));
		cb.endText();

		cb.beginText();
		cb.moveText(346, 665);
		cb.setFontAndSize(BaseFont.createFont(), 10);
		cb.showText(Integer.toString(reinforcement.getRequiredNumberOfSymmetricalRodsAS1()));
		cb.endText();

		cb.beginText();
		cb.moveText(365, 665);
		cb.setFontAndSize(BaseFont.createFont(), 10);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getDesignedDiameterSymmetricalAS1()));
		cb.endText();

		// as2
		cb.beginText();
		cb.moveText(290, 653);
		cb.setFontAndSize(BaseFont.createFont(), 10);
		cb.showText(PrintFormatter.as1as2PrintFormatter(reinforcement.getDesignedSymmetricalAS2()));
		cb.endText();

		cb.beginText();
		cb.moveText(346, 652);
		cb.setFontAndSize(BaseFont.createFont(), 10);
		cb.showText(Integer.toString(reinforcement.getRequiredNumberOfSymmetricalRodsAS2()));
		cb.endText();

		cb.beginText();
		cb.moveText(365, 652);
		cb.setFontAndSize(BaseFont.createFont(), 10);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getDesignedDiameterSymmetricalAS2()));
		cb.endText();

		// vertical sitruups

		cb.beginText();
		cb.moveText(365, 627);
		cb.setFontAndSize(BaseFont.createFont(), 10);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getaSW1Diameter()));
		cb.endText();

		cb.beginText();
		cb.moveText(292, 613);
		cb.setFontAndSize(BaseFont.createFont(), 10);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getnS1()));
		cb.endText();

		cb.beginText();
		cb.moveText(330, 613);
		cb.setFontAndSize(BaseFont.createFont(), 10);
		cb.showText(PrintFormatter.s1s2(reinforcement.getS1Required()));
		cb.endText();

		// bent rods
		cb.beginText();
		cb.moveText(365, 599);
		cb.setFontAndSize(BaseFont.createFont(), 10);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getaSW2Diameter()));
		cb.endText();

		cb.beginText();
		cb.moveText(292, 585);
		cb.setFontAndSize(BaseFont.createFont(), 10);
		cb.showText(OutputFormatter.formatAs1As2(reinforcement.getnS2Designed()));
		cb.endText();

		cb.beginText();
		cb.moveText(330, 585);
		cb.setFontAndSize(BaseFont.createFont(), 10);
		cb.showText(PrintFormatter.s1s2(reinforcement.getS2Required()));
		cb.endText();
		// sls

		document.close();
	}

	private static ArrayList<String> resultsList = new ArrayList();
	private static ArrayList<String> variableList = new ArrayList();

	private static void printResultsForJacekIntoSecondFile() throws IOException, DocumentException {

		File file = new File(GetExecutionPathForSecondFile());
		file.getParentFile().mkdirs();
		createPdfForSecondaryResults(GetExecutionPathForSecondFile());
	}

	public static void clearResults() {
		resultsList.clear();
		variableList.clear();
	}

	public static void addResults(String variable, String result) {
		variableList.add(variable);
		resultsList.add(result);
	}

	private static void createPdfForSecondaryResults(String dest) throws IOException, DocumentException {
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(dest));
		document.open();
		PdfPTable table = new PdfPTable(2);
		for (int i = 0; i < resultsList.size(); i++) {
			table.addCell(variableList.get(i));
			table.addCell(resultsList.get(i));

		}
		document.add(table);
		document.close();
	}

}
