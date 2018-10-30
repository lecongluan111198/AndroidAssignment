package com.example.user.work_5;

import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    TextView txtResult;
    ProgressBar progressBar;
    Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStart = (Button)findViewById(R.id.btnRead);
        txtResult = (TextView)findViewById(R.id.txtMsg);
        progressBar = (ProgressBar)findViewById(R.id.pgb);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputStream is =  getResources().openRawResource(R.raw.golfers);

                (new XMLParse(is)).execute("Name","Phone");
            }
        });
    }





    class XMLParse extends AsyncTask<String, Integer, String>{

        int process = 0, elementCount = 0;
        InputStream is;

        public XMLParse(InputStream is) {
            super();
            this.is = is;
        }

        @Override
        protected String doInBackground(String... params) {
            int n = params.length; // total number of parameters

            String[] elementName = new String[n]; // element names
            for (int i = 0; i < n; i++) elementName[i] = params[i];
            StringBuilder str = new StringBuilder();

            try {
                DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document document = documentBuilder.parse(is);

                if (document == null) {
                    Log.v("REALLY BAD!!!!", "document was NOT made by parser");
                    return "BAD-ERROR";
                }

                NodeList[] elementList = new NodeList[n];
                process = 0;
                // dem element
                for (int i = 0; i < n; i++) {
                    elementList[i] = document.getElementsByTagName(elementName[i]);
                    elementCount += elementList.length;
                }
                progressBar.setMax(elementCount);


                // xu ly
                for (int i = 0; i < n; i++) {
                    elementList[i] = document.getElementsByTagName(elementName[i]);

                    str.append(getTextAndAttributesFromNode(elementList[i], elementName[i]));
                }

            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return str.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            txtResult.setText(s);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(process);
        }

        private Object getTextAndAttributesFromNode(NodeList list, String strElementName) {
            StringBuilder str = new StringBuilder();
            // dealing with the <strElementName> tag
            str.append("\n\nNodeList for: <" + strElementName + "> Tag");
            for (int i = 0; i < list.getLength(); i++) {
                // extract TEXT enclosed inside <element> tags
                Node node = list.item(i);
                String text = node.getTextContent();
                str.append("\n " + i + ": " + text);
                // get ATTRIBUTES inside the current element
                int size = node.getAttributes().getLength();
                for (int j = 0; j < size; j++) {
                    String attrName = node.getAttributes().item(j).getNodeName();
                    String attrValue = node.getAttributes().item(j).getNodeValue();
                    str.append("\n attr. info-" + i + "-" + j + ": " + attrName
                            + " " + attrValue);

                    process++;
                    onProgressUpdate(process);
                }

            }
            return str;
        }
    }

}
