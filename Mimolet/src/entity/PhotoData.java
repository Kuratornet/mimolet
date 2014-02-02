package entity;

public class PhotoData {
	private int photoStyle;
	private String backgroundColorString;
	private String borderColorString;
	private String textFontType;
	private String text;
	private float textFontSize;
	private String textTextColorString;
	private int backgroundColor;
	private int borderColor;
	private int textTextColor;
	public int getPhotoStyle() {
		return photoStyle;
	}
	public void setPhotoStyle(int photoStyle) {
		this.photoStyle = photoStyle;
	}
	public String getBackgroundColorString() {
		return backgroundColorString;
	}
	public void setBackgroundColorString(String backgroundColorString) {
		this.backgroundColorString = backgroundColorString;
	}
	public String getBorderColorString() {
		return borderColorString;
	}
	public void setBorderColorString(String borderColorString) {
		this.borderColorString = borderColorString;
	}
	public String getTextFontType() {
		return textFontType;
	}
	public void setTextFontType(String textFontType) {
		this.textFontType = textFontType;
	}
	public float getTextFontSize() {
		return textFontSize;
	}
	public void setTextFontSize(float textFontSize) {
		this.textFontSize = textFontSize;
	}
	public String getTextTextColorString() {
		return textTextColorString;
	}
	public void setTextTextColorString(String textTextColorString) {
		this.textTextColorString = textTextColorString;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
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
	public int getTextTextColor() {
		return textTextColor;
	}
	public void setTextTextColor(int textTextColor) {
		this.textTextColor = textTextColor;
	}
	
}
