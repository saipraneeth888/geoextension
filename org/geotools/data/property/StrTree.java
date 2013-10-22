package org.geotools.data.property;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.nocrala.tools.gis.data.esri.shapefile.ShapeFileReader;
import org.nocrala.tools.gis.data.esri.shapefile.exception.InvalidShapeFileException;
import org.nocrala.tools.gis.data.esri.shapefile.header.ShapeFileHeader;
import org.nocrala.tools.gis.data.esri.shapefile.shape.AbstractShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolygonShape;


import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.strtree.STRtree;

public class StrTree {

	/**
	 * @param args
	 */
	STRtree index= new STRtree();
	private static StrTree instance = null;
	protected StrTree() throws IOException{
		STRtree index = new STRtree();
    	ArrayList attTwo = new ArrayList();
		FileInputStream is = new FileInputStream(
		            "C:\\users\\saipraneeth\\downloads\\spatial_data\\spatial_data\\periodic_table\\periodic_table.shp");
		        ShapeFileReader r;
				try {
					r = new ShapeFileReader(is);
					  ShapeFileHeader h = r.getHeader();
				        ArrayList<Double> pointsForFile = null;
				   //     System.out.println(r);
				  //      System.out.println("The shape type of this files is " + h.getShapeType());
				        int total = 0;
				        AbstractShape shape;
				        try {
							while ((shape = r.next()) != null) {
							    PolygonShape aPolygon = (PolygonShape) shape;
							   Envelope itemEnv = new Envelope(aPolygon.getBoxMaxX(),aPolygon.getBoxMinX(),aPolygon.getBoxMaxY(),aPolygon.getBoxMinY());
							    index.insert(itemEnv, aPolygon);
							  }
						} catch (InvalidShapeFileException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				} catch (InvalidShapeFileException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
		      
		          
		        index.build();
	}
	public static StrTree getInstance() throws IOException{
		 if(instance == null) {
	         instance = new StrTree();
	      }
	      return instance;
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
	
	}

}
