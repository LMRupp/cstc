package de.usd.cstchef.operations.extractors;

import java.util.Arrays;

import javax.swing.JCheckBox;

import burp.BurpUtils;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.core.ByteArray;
import burp.api.montoya.http.message.params.HttpParameterType;
import burp.api.montoya.http.message.requests.HttpRequest;
import de.usd.cstchef.Utils.MessageType;
import de.usd.cstchef.operations.Operation;
import de.usd.cstchef.operations.Operation.OperationInfos;
import de.usd.cstchef.operations.OperationCategory;

@OperationInfos(name = "HTTP URI", category = OperationCategory.EXTRACTORS, description = "Extracts the URI of a HTTP request.")
public class HttpUriExtractor extends Operation {

    private JCheckBox checkbox;

    @Override
    public void createUI() {
        this.checkbox = new JCheckBox("With parameters");
        this.checkbox.setSelected(true);
        this.addUIElement(null, this.checkbox, "checkbox1");
    }

    @Override
    protected ByteArray perform(ByteArray input, MessageType messageType) throws Exception {
        try{
            return ByteArray.byteArray(HttpRequest.httpRequest(input).url());
        }
        catch(Exception e){
            throw new IllegalArgumentException("Input is not a valid request");
        }
    }
}
