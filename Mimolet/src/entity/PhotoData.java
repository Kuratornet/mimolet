package entity;

public class PhotoData {
	private int photoStyle;
	private int backgroundColor;
	private int borderColor;
	private String textFontType;
	private String text;
	private float textFontSyze;
	private int textTextColor;
	public int getPhotoStyle() {
		return photoStyle;
	}
	public void setPhotoStyle(int photoStyle) {
		this.photoStyle = photoStyle;
	}
	public int getBackgroundColor() {
		return backgroundColor;
	}
	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	public int getBorderColor() {
		return borderColor;
	}
	public void setBorderColor(int borderColor) {
		this.borderColor = borderColor;
	}
	public String getTextFontType() {
		return textFontType;
	}
	public void setTextFontType(String textFontType) {
		this.textFontType = textFontType;
	}
	public float getTextFontSyze() {
		return textFontSyze;
	}
	public void setTextFontSyze(float textFontSyze) {
		this.textFontSyze = textFontSyze;
	}
	public int getTextTextColor() {
		return textTextColor;
	}
	public void setTextTextColor(int textTextColor) {
		this.textTextColor = textTextColor;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}
