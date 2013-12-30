package de.uni.freiburg.iig.telematik.swat.editor.actions;

import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;



	public class ImageFileFilter extends FileFilter
	{

		/**
		 * Holds the accepted file format extensions for images.
		 */
		protected static String[] imageFormats = ImageIO.getReaderFormatNames();

		/**
		 * Description of the filter.
		 */
		protected String desc;

		/**
		 * Constructs a new file filter for all supported image formats using
		 * the specified description.
		 * 
		 * @param description
		 *            The description to use for the file filter.
		 */
		public ImageFileFilter(String description)
		{
			desc = description;
		}

		/**
		 * Returns true if the file is a directory or ends with a known image
		 * extension.
		 * 
		 * @param file
		 *            The file to be checked.
		 * @return Returns true if the file is accepted.
		 */
		public boolean accept(File file)
		{
			if (file.isDirectory())
			{
				return true;
			}

			String filename = file.toString().toLowerCase();

			for (int j = 0; j < imageFormats.length; j++)
			{
				if (filename.endsWith("." + imageFormats[j].toLowerCase()))
				{
					return true;
				}
			}

			return false;
		}

		/**
		 * Returns the description.
		 * 
		 * @return Returns the description.
		 */
		public String getDescription()
		{
			return desc;
		}

	}

