package de.feelx88.cgai.test;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class X3DParser extends DefaultHandler {
	
	public ArrayList<Short> mIndices = new ArrayList<Short>();
	public ArrayList<Float> mVertices = new ArrayList<Float>();
	public ArrayList<Float> mNormals = new ArrayList<Float>();
	public ArrayList<Float> mColors = new ArrayList<Float>();
	public ArrayList<Float> mTexCoords = new ArrayList<Float>();
	public ArrayList<String> mTextures = new ArrayList<String>();
	
	@Override
	public void startElement(String uri, String localName, String qName, 
			Attributes attributes) throws SAXException
	{
		if( localName.equals( "IndexedFaceSet" ) )
		{
			StringTokenizer t = new StringTokenizer( attributes.getValue( "coordIndex" ) );
			while( t.hasMoreElements() )
			{
				String next = (String)t.nextElement();
				if( !next.equals( "-1," ) )
					mIndices.add( Short.parseShort( next ) );
			}
		}
		if( localName.equals( "Coordinate" ) )
		{
			StringTokenizer t = new StringTokenizer( attributes.getValue( "point" ), " ," );
			while( t.hasMoreElements() )
				mVertices.add( Float.parseFloat( t.nextToken() ) );
		}
		if( localName.equals( "Normals" ) )
		{
			StringTokenizer t = new StringTokenizer( attributes.getValue( "point" ), " ," );
			while( t.hasMoreElements() )
				mNormals.add( Float.parseFloat( t.nextToken() ) );
		}
		if( localName.equals( "TextureCoordinate" ) )
		{
			StringTokenizer t = new StringTokenizer( attributes.getValue( "point" ), " ," );
			while( t.hasMoreElements() )
				mTexCoords.add( Float.parseFloat( t.nextToken() ) );
		}
		if( localName.equals( "ImageTexture" ) )
		{
			StringTokenizer t = new StringTokenizer( attributes.getValue( "url" ), " \"" );
			mTextures.add( t.nextToken() );
		}
	}
}