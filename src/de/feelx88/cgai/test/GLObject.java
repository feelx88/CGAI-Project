package de.feelx88.cgai.test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.opengles.GL10;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class GLObject {
	
	private FloatBuffer mVertices, mNormals, mColors, mTexCoords;
	private ShortBuffer mIndices;
	
	private int mTexIds[] = { -1 };
	private Bitmap mTexBitmap = null;
	
	public float mPosition[] = { 0, 0, 0 }, mRotation[] = { 0, 0, 1, 0 }, mColor[] = { 1, 1, 1, 1 };
	
	private boolean mInitialized = false, mLoadTexture = false;
	
	public void draw( GL10 gl )
	{
		if( !mInitialized )
			return;
		
		gl.glPushMatrix();
		
		gl.glTranslatef( mPosition[0], mPosition[1], mPosition[2] );
		
		gl.glRotatef( mRotation[0], mRotation[1], mRotation[2], mRotation[3] );

		mIndices.position( 0 );	
		mVertices.position( 0 );
		mNormals.position( 0 );
		mColors.position( 0 );
		mTexCoords.position( 0 );
		
		gl.glEnableClientState( GL10.GL_VERTEX_ARRAY );
		gl.glVertexPointer( 3, GL10.GL_FLOAT, 0, mVertices );
		
		if( mNormals.capacity() > 0 )
		{
			gl.glEnableClientState( GL10.GL_NORMAL_ARRAY );
			gl.glNormalPointer( GL10.GL_FLOAT, 0, mNormals );
		}
		
		if( mColors.capacity() >  0)
		{
			gl.glEnableClientState( GL10.GL_COLOR_ARRAY );
			gl.glColorPointer( 4, GL10.GL_FLOAT, 0, mColors );
		}
		else
			gl.glColor4f( mColor[0], mColor[1], mColor[2], mColor[3] );
		
		if( mLoadTexture )
		{
			gl.glGenTextures( 1, mTexIds, 0 );
			gl.glBindTexture( GL10.GL_TEXTURE_2D, mTexIds[0] );
			
            gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR );
            gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR );
			
            GLUtils.texImage2D( GL10.GL_TEXTURE_2D, 0, mTexBitmap, 0 );
			
			mLoadTexture = false;
		}
		
		if( mTexIds[0] != -1 && mTexCoords.capacity() > 0 )
		{
			gl.glEnable( GL10.GL_TEXTURE_2D );
			gl.glEnableClientState( GL10.GL_TEXTURE_COORD_ARRAY );
			gl.glTexCoordPointer( 2, GL10.GL_FLOAT, 0, mTexCoords );
			gl.glBindTexture( GL10.GL_TEXTURE_2D, mTexIds[0] );
		}
		
		gl.glDrawElements( GL10.GL_TRIANGLES, mIndices.capacity(), GL10.GL_UNSIGNED_SHORT, mIndices );
		
		gl.glDisableClientState( GL10.GL_VERTEX_ARRAY );
		gl.glDisableClientState( GL10.GL_NORMAL_ARRAY );
		gl.glDisableClientState( GL10.GL_COLOR_ARRAY );
		gl.glDisableClientState( GL10.GL_TEXTURE_COORD_ARRAY );
		
		gl.glDisable( GL10.GL_TEXTURE_2D );	
	
		gl.glPopMatrix();
	}
	
	void loadFile( InputStream file ) throws SAXException, IOException, ParserConfigurationException
	{
		//Parse
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		X3DParser handler = new X3DParser();
		parser.parse( file, handler );
		
		//Indices
		ByteBuffer tmp = ByteBuffer.allocateDirect( handler.mIndices.size() * 2 );
		tmp.order( ByteOrder.nativeOrder() );
		mIndices = tmp.asShortBuffer();
		for( int x = 0; x < handler.mIndices.size(); x++ )
			mIndices.put( handler.mIndices.get( x ) );
		
		//Vertices
		tmp = ByteBuffer.allocateDirect( handler.mVertices.size() * 4 );
		tmp.order( ByteOrder.nativeOrder() );
		mVertices = tmp.asFloatBuffer();
		for( int x = 0; x < handler.mVertices.size(); x++ )
			mVertices.put( handler.mVertices.get( x ) );
		
		//Normals
		tmp = ByteBuffer.allocateDirect( handler.mNormals.size() * 4 );
		tmp.order( ByteOrder.nativeOrder() );
		mNormals = tmp.asFloatBuffer();
		for( int x = 0; x < handler.mNormals.size(); x++ )
			mNormals.put( handler.mNormals.get( x ) );
		
		//Colors
		tmp = ByteBuffer.allocateDirect( handler.mColors.size() * 4 );
		tmp.order( ByteOrder.nativeOrder() );
		mColors = tmp.asFloatBuffer();
		for( int x = 0; x < handler.mColors.size(); x++ )
			mColors.put( handler.mColors.get( x ) );
		
		//TexCoords
		tmp = ByteBuffer.allocateDirect( handler.mTexCoords.size() * 4 );
		tmp.order( ByteOrder.nativeOrder() );
		mTexCoords = tmp.asFloatBuffer();
		for( int x = 0; x < handler.mTexCoords.size(); x++ )
			mTexCoords.put( handler.mTexCoords.get( x ) );
		
		if( handler.mTextures.size() > 0 )
		{
			mTexBitmap = BitmapFactory.decodeStream( 
					ClearRenderer.sAssets.open( handler.mTextures.get( 0 ) ) );
			if( mTexBitmap != null )
				mLoadTexture = true;
		}
		
		//Allow rendering
		mInitialized = true;
	}
}
