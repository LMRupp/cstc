package de.usd.cstchef.operations.setter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;

import javax.swing.JCheckBox;

import burp.BurpUtils;
import burp.IBurpExtenderCallbacks;
import burp.IExtensionHelpers;
import burp.IRequestInfo;
import de.usd.cstchef.operations.Operation;
import de.usd.cstchef.operations.OperationCategory;
import de.usd.cstchef.operations.Operation.OperationInfos;
import de.usd.cstchef.view.ui.FormatTextField;
import de.usd.cstchef.view.ui.VariableTextField;

@OperationInfos(name = "HTTP SET Uri", category = OperationCategory.SETTER, description = "Sets the given variable as the uri.")
public class HttpSetUri extends Operation {

	private VariableTextField uriTxt;
	private JCheckBox checkbox;

	@Override
	public void createUI() {
		this.uriTxt = new VariableTextField();
		this.addUIElement("Uri", this.uriTxt);
		
		this.checkbox = new JCheckBox("Keep parameters");
	    this.checkbox.setSelected(false);
		this.addUIElement(null, this.checkbox);
	}

	
	@Override
	protected byte[] perform(byte[] input) throws Exception {
		try {
			IBurpExtenderCallbacks callbacks = BurpUtils.getInstance().getCallbacks();
			IExtensionHelpers helpers = callbacks.getHelpers();
			int length = input.length;
			
			int firstMark = helpers.indexOf(input, " ".getBytes(), false, 0, length);
			int secondMark;
			
			if( this.checkbox.isSelected() ) {
				secondMark = helpers.indexOf(input, "?".getBytes(), false, firstMark + 1, length);
			} else {
				secondMark = helpers.indexOf(input, " ".getBytes(), false, firstMark + 1, length);
			}
			
			byte[] method = Arrays.copyOfRange(input, 0, firstMark + 1);
			byte[] newUri = this.uriTxt.getBytes();
			byte[] rest = Arrays.copyOfRange(input, secondMark, length);
			
			byte[] newRequest = new byte[method.length + newUri.length + rest.length];
			System.arraycopy(method, 0, newRequest, 0, method.length);
			System.arraycopy(newUri, 0, newRequest, method.length, newUri.length);
			System.arraycopy(rest, 0, newRequest, method.length + newUri.length, rest.length);
			
			return newRequest;
			
		} catch (Exception e) {
			throw new IllegalArgumentException("Provided input is not a valid http request.");
		}
	}

}
