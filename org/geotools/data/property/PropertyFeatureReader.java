package org.geotools.data.property;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.geotools.data.FeatureReader;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.util.logging.Logging;
import org.json.JSONException;
import org.json.JSONObject;
import org.nocrala.tools.gis.data.esri.shapefile.ShapeFileReader;
import org.nocrala.tools.gis.data.esri.shapefile.exception.InvalidShapeFileException;
import org.nocrala.tools.gis.data.esri.shapefile.header.ShapeFileHeader;
import org.nocrala.tools.gis.data.esri.shapefile.shape.AbstractShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolygonShape;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.strtree.STRtree;

/**
 * FeatureReader access to the contents of a PropertyFile.
 * 
 * @author Jody Garnett
 * @version 8.0.0
 * @since 2.0.0
 */
/*
 * Sai
 * task: in next() function
 *  values[reader.getAttributeCount()-1] = 10; this is the line that I added
 *  It assigns all the srval's to 10.Which you can check in property example
 *  task: change this to read from array.
 */
public class PropertyFeatureReader implements
        FeatureReader<SimpleFeatureType, SimpleFeature> {
    private static final Logger LOGGER = Logging
            .getLogger("org.geotools.data.property");

    PropertyAttributeReader reader;

    /**
     * Creates a new PropertyFeatureReader object.
     * 
     * @param directory Directory containing property file
     * @param typeName TypeName indicating file to read
     * @throws IOException
     */
    public PropertyFeatureReader(File directory, String typeName)
            throws IOException {
        File file = new File(directory, typeName + ".properties");
        reader = new PropertyAttributeReader(file);
    }

    /**
     * Access to schema description.
     * 
     * @return SimpleFeatureType describing attribtues
     */
    public SimpleFeatureType getFeatureType() {
        return reader.type;
    }

    /**
     * Access the next feature (if available).
     * 
     * @return SimpleFeature read from property file
     * @throws IOException
     * @throws IllegalAttributeException
     * @throws NoSuchElementException
     */
    public SimpleFeature next() throws IOException, IllegalAttributeException,
            NoSuchElementException {
        reader.next();
        SimpleFeatureType type = reader.type;
        String fid = reader.getFeatureID();
    	String s = fid.substring(3);
		int d = Integer.parseInt(s);
        Object[] values = new Object[reader.getAttributeCount()];

        for (int i = 0; i < reader.getAttributeCount()-1; i++) {
            try {
                values[i] = reader.read(i);
            } catch (RuntimeException e) {
                values[i] = null;
            } catch (IOException e) {
                throw e;
            }
        }
    	StrTree ind = new StrTree();
		ind.getInstance();
    /*	STRtree index = new STRtree();
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
	//	        System.out.println(index.size());
		        Envelope itemEnv = new Envelope(2811.1033555166046,78.6638605853459,-212.7334765609122,-2212.6466388084727);
				List<PolygonShape> a =index.query(itemEnv);
				for(PolygonShape b:a){
		//			System.out.println(b.getNumberOfParts()+"-" + b.getNumberOfPoints());
					 ArrayList p = new ArrayList();
			              PointData[] points = b.getPointsOfPart(0);
			              for(int j=0;j<points.length;j++){
			            	  p.add(points[j].getX());
			            	  p.add(points[j].getY());
			              }
			              String geomP="POLYGON((";
			              geomP = geomP + p.get(0);
			              for(int k=1;k<p.size();k++){
			            	  if(k%2 == 0)
			            		  geomP = geomP + "," + p.get(k);
			            	  else
			            		  geomP = geomP+ " " + p.get(k);
			              }
			              geomP = geomP + "))";
			               attTwo.add(geomP);
				}
				values[reader.getAttributeCount()-2] = attTwo.get(d);*/
        File f = new File("C:\\resource.txt");
		BufferedReader fr = new BufferedReader(new FileReader(f));
		String id = fr.readLine();
		String srUrl = "http://localhost:8080/atlasify-web/getsr.php?q=";
		String finalUrl = srUrl + id;
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(finalUrl);
		HttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();
		String fromPhp = EntityUtils.toString(entity);
		String dataArray[] = fromPhp.split("<");
		String number = dataArray[0];
		String s1 = "{menu:" + number+"}";
		JSONObject jsonObj;
		JSONObject menu;
		try {
			jsonObj = new JSONObject(s1);
			menu = jsonObj.getJSONObject("menu");
			Iterator iter = menu.keys();
			int count = 0;
			Float[] srval= new Float[119];
			iter.next();
			while (iter.hasNext())
			{
				String key = (String)iter.next();
				srval[count] = (float)menu.getDouble(key);
				count++;
			}
				for(int i=0;i<117;i++)
				{
					//System.out.println(srval[i]);
					if(srval[i].compareTo(0.1f)<0)
						srval[i]=0.0f;
					else if(srval[i].compareTo(0.2f)<0 && srval[i].compareTo(0.1f)>0)
						srval[i] = 1.0f;
					else if(srval[i].compareTo(0.3f)<0 && srval[i].compareTo(0.2f)>0)
						srval[i] = 2.0f;
					else if(srval[i].compareTo(0.4f)<0 && srval[i].compareTo(0.3f)>0)
						srval[i] = 3.0f;
					else if(srval[i].compareTo(0.5f)<0 && srval[i].compareTo(0.4f)>0)
						srval[i] = 4.0f;
					else if(srval[i].compareTo(0.6f)<0 && srval[i].compareTo(0.5f)>0)
						srval[i] = 5.0f;
					else if(srval[i].compareTo(0.7f)<0 && srval[i].compareTo(0.6f)>0)
						srval[i] = 6.0f;
					else if(srval[i].compareTo(0.8f)<0 && srval[i].compareTo(0.7f)>0)
						srval[i] = 7.0f;
					else if(srval[i].compareTo(0.9f)<0 && srval[i].compareTo(0.8f)>0)
						srval[i] = 8.0f;
					else if(srval[i].compareTo(1.0f)<0 && srval[i].compareTo(0.9f)>0)
						srval[i] = 9.0f;
					else if(srval[i].compareTo(1.0f)==0)
						srval[i] = 10.0f;
				
					//System.out.println(srval[i]);
				}	
			    values[reader.getAttributeCount()-1] = srval[d];			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
      } 
      return SimpleFeatureBuilder.build(type, values, fid);
    }

    /**
     * Check if additional contents are available.
     * 
     * @return <code>true</code> if additional contents are available
     * @throws IOException
     */
    public boolean hasNext() throws IOException {
        return reader.hasNext();
    }

    /**
     * Close the FeatureReader when not in use.
     * 
     * @throws IOException
     */
    public void close() throws IOException {
        if (reader == null) {
            LOGGER.warning("Stream seems to be already closed.");
        } else {
            reader.close();
        }
        reader = null;
    }
}
