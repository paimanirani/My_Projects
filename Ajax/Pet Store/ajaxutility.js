/*
                   Ajax Utility JavaScript Library
            ** No warranty is expressed or implied. **

  This JavaScript library supports Ajax.

  Place ajaxutility.js in the same directory as your web pages and use this code 
  in the <head> section of your pages:

  <script type = "text/javascript" src = "ajaxutility.js"></script>

  This library supports these functions: 

  getText(urlToCall, functionToCallBack) 
    Uses the GET method to get text from the server. 

  getXml(urlToCall, functionToCallBack) 
    Uses the GET method to get XML from the server. 

  postDataReturnText(urlToCall, dataToSend, functionToCallBack) 
    Uses the POST method to send dataToSend to server, gets text back. 
    Pass the data to send in parameter/value pairs like this: "value=100".

  postDataReturnXml(urlToCall, dataToSend, functionToCallBack) 
    Uses the POST method to send dataToSend to server, gets XML back. 
    Pass the data to send in parameter/value pairs like this: "value=100".

*/

function getText(urlToCall, functionToCallBack)
{ 
  var XMLHttpRequestObject = false; 

  if (window.XMLHttpRequest) {
    XMLHttpRequestObject = new XMLHttpRequest();
  } else if (window.ActiveXObject) {
    XMLHttpRequestObject = new 
     ActiveXObject("Microsoft.XMLHTTP");
  }

  if(XMLHttpRequestObject) {
    XMLHttpRequestObject.open("GET", urlToCall); 

    XMLHttpRequestObject.onreadystatechange = function() 
    { 
      if (XMLHttpRequestObject.readyState == 4 && 
        XMLHttpRequestObject.status == 200) { 
          functionToCallBack(XMLHttpRequestObject.responseText); 
          delete XMLHttpRequestObject;
          XMLHttpRequestObject = null;
      } 
    } 

    XMLHttpRequestObject.send(null); 
  }
}

function getXml(urlToCall, functionToCallBack)
{ 
  var XMLHttpRequestObject = false; 

  if (window.XMLHttpRequest) {
    XMLHttpRequestObject = new XMLHttpRequest();
    if (XMLHttpRequestObject.overrideMimeType)
      XMLHttpRequestObject.overrideMimeType("text/xml");
  } else if (window.ActiveXObject) {
    XMLHttpRequestObject = new 
     ActiveXObject("Microsoft.XMLHTTP");
  }

  if(XMLHttpRequestObject) {
    XMLHttpRequestObject.open("GET", urlToCall); 

    XMLHttpRequestObject.onreadystatechange = function() 
    { 
      if (XMLHttpRequestObject.readyState == 4 && 
        XMLHttpRequestObject.status == 200) { 
          functionToCallBack(XMLHttpRequestObject.responseXML); 
          delete XMLHttpRequestObject;
          XMLHttpRequestObject = null;
      } 
    } 

    XMLHttpRequestObject.send(null); 
  }
}

function postDataGetText(urlToCall, dataToSend, functionToCallBack)
{ 
  var XMLHttpRequestObject = false; 

  if (window.XMLHttpRequest) {
      XMLHttpRequestObject = new XMLHttpRequest();
  } else if (window.ActiveXObject) {
    XMLHttpRequestObject = new 
     ActiveXObject("Microsoft.XMLHTTP");
  }

  if(XMLHttpRequestObject) {
    XMLHttpRequestObject.open("POST", urlToCall); 
    XMLHttpRequestObject.setRequestHeader('Content-Type', 
      'application/x-www-form-urlencoded'); 

    XMLHttpRequestObject.onreadystatechange = function() 
    { 
      if (XMLHttpRequestObject.readyState == 4 && 
        XMLHttpRequestObject.status == 200) {
          functionToCallBack(XMLHttpRequestObject.responseText); 
          delete XMLHttpRequestObject;
          XMLHttpRequestObject = null;
      } 
    }

    XMLHttpRequestObject.send(dataToSend); 
  }
}

function postDataGetXml(urlToCall, dataToSend, functionToCallBack)
{ 
  var XMLHttpRequestObject = false; 

  if (window.XMLHttpRequest) {
    XMLHttpRequestObject = new XMLHttpRequest();
    if (XMLHttpRequestObject.overrideMimeType)
      XMLHttpRequestObject.overrideMimeType("text/xml");
  } else if (window.ActiveXObject) {
    XMLHttpRequestObject = new 
     ActiveXObject("Microsoft.XMLHTTP");
  }

  if(XMLHttpRequestObject) {
    XMLHttpRequestObject.open("POST", urlToCall); 
    XMLHttpRequestObject.setRequestHeader('Content-Type', 
      'application/x-www-form-urlencoded'); 

    XMLHttpRequestObject.onreadystatechange = function() 
    { 
      if (XMLHttpRequestObject.readyState == 4 && 
        XMLHttpRequestObject.status == 200) {
          functionToCallBack(XMLHttpRequestObject.responseXML); 
          delete XMLHttpRequestObject;
          XMLHttpRequestObject = null;
      } 
    }

    XMLHttpRequestObject.send(dataToSend); 
  }
}

