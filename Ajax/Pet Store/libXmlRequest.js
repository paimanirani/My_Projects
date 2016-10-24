/*
	libXmlRequest Library v. [ ADD: new version ]
	Author: Stephen W. Cote
	Email: wranlon@hotmail.com
	
	Copyright 2002 - 2005 All Rights Reserved.
	
	[ ADD: new license ]

	[ ADD: other changes since 2003; there were a few, I think ]

	REMARKS

		The org, org.cote, and org.cote.js defs are a literal translation from the Engine project.
		The structure is left intact for compatibility.
	
		The package structure is considered part of the branding and copyright; do not alter it.

	BROWSER SUPPORT
		
		Mozilla-based browsers (eg: Mozilla, Firebird, NS 7)
			XSL and XPath supported as of 1.2

		Internet Explorer 5.01 and later
			MSXML Support is defined as MSXML2.XMLHTTP.3.0

		Safari 1.0.3: XMLHttpRequest, no XPath, no XSL

		Opera 8: XMLHttpRequest, no XPath, no XSL

		Konqueror: No / unknown
	
	USAGE
	
		Synchronous GET:
			[xml_dom_object] = getXml(path);

			Example:
			var oXml = org.cote.js.xml.getXml("/Data/Test1.xml");

		Asynchronous GET:
			[int] = getXml(path,custom_handler,1,{optional_id});

			Example:
			function HandleXml(s,v){
				var oXml = v.xdom;
			}
			org.cote.js.xml.getXml("/Data/Test1.xml",HandleXml,1);
			
		Cached Asynchronous GET:
			[int] = getXml(path,custom_handler,1,request_id,1);
			
			Example:
			function HandleXml(s,v){
				var oXml = v.xdom;
			}
			org.cote.js.xml.getXml("/Data/Test1.xml",HandleXml,1,"cache-me",1);
			
		Synchronous POST:
			[xml_dom_object] = postXml(path,data);
			
			Example:
			
			var oPostThis = org.cote.js.xml.newXmlDocument("Request");
			var oData = oPostThis.createElement("data");
			oData.setAttribute("id","data-id");
			oData.setAttribute("value","data-value");
			oPostThis.documentElement.appendChild(oData);
			
			var oResponseXml = org.cote.js.xml.postXml("/Data/TestData.aspx",oPostThis);
			
		Asynchronous POST:
			[int] = postXml(path,data,custom_handler,1,{optional_id});
			
			Example:
			
			
			function HandleXml(s,v){
				var oResponseXml = v.xdom;
			}
			var oPostThis = org.cote.js.xml.newXmlDocument("Request");
			var oResponseXml = org.cote.js.xml.postXml("/Data/TestData.aspx",oPostThis,HandlePostXml,1);
			

		Notes:
			custom_handler, required for asynchronous requests, is invoked with two parameters:
			"onloadxml", and a generic object.  The object includes two properties:
			object.id is the request id, and object.xdom refers to the XML DOM.
			If the request fails, object.xdom will be null.
			
	INTERNAL NOTES
		
		The spec for XMLHttpRequest specifies that all handlers are cleared after each request.
		Therefore, the internal handlers are cleared after each request.  If not, they don't fire on subsequent requests.

	BUGS and BUG FIXES

		09/09/2005 : clearCache wasn't actually clearing the cache.		
			Some method cleanup required.
			
			The internal event handler, and therefore any custom event handler, for asynchronous calls that fail due to exceeding the pool size, was not being called.  This has been fixed.
			
		09/08/2005 : Improve support
			Improved support for Opera and Safari.

		06/17/2003 : Race condition occurs in multiple requests using the same request id
			Status: fixed
			
			XML Requests are stored in an array so as to keep track of the request for asynchronous and synchronous actions.
			A property on a request item is used for caching, if caching is enabled.

			Whether caching is enabled or the request is synchronous or asynchronous,
			a race condition ensues if multiple requests are made at the same time, with the same id.
			The bug is the first request is incomplete, and subsequent requests fail in cache request,
			or fail in the internal request handler.
			
			The solution was to tack on a unique back-up id if the same-id request is incomplete.
			This results in two requests for the same XML file, but the result is returned with the original request id.
			If caching is enabled, then the cache return the same-id request once the first request is finished.

			This was originally fixed only for cached, synchronous requests, but then expanded to included all requests.


		05/30/2003 : Event handler problem in Mozilla 1.4RC1
			Status: fixed; duplicate clean-up still open

			Latest Mozilla build, 1.4RC1 won't use the same event listener for the same object.
			Solution: add event listener at the time of the request, then strip it off later.
			
			This will also clean up the duplicate code for adding event listeners for pooled and non-pooled objects, which is pretty much the same.

		05/08/2003 : Event handler problem in IE
			Status: fixed; refer race-condition fix
			
			The IE sync bug cropped up again outside of the cached http feature.
			If the feature is turned off, it seems the requests can get
			stacked up (see Engine Demo #10 in IE), and cause the browser to hang
			or fail (see Moz) to completely load the XML.
			
			Multiple requests seem to be ok, up to an undetermined point.
			
			The feature should be left intact for heavy use anyway, but it is crucial to
			note because it then becomes a requirement for heavy use.

		12/28/2002
			Status: fixed
			
			Fixed bug in returnXmlHttpObjectToPool; this was causing a monster headache!




pseudo code for selectSingleNode / selectNodes

window.selectNodes = function (d,v,c){
  var elName = v.replace(/[^\w].*$/,'');
  elName = d.getElementsByTagName(elName);
  var attrToMatch = v.replace(/^.*@/,'').replace(/\s*=.*$/,'');
  var valToMatch = v.replace(/^[^']*'/,'').replace(/'.*$/,'');
  for(var i=0;elName[i];i++){if(elName[i].getAttribute(attrToMatch)==valToMatch){return [elName[i]];}}
  return [];
}
*/


var org = {};
org.cote = {};
org.cote.js = {};

org.cote.js.xml = {
	object_version:"SPEC-1.1.102.1706.2003",
	counter:0,

	_xml_http_cache_enabled:1,
	_xml_requests:[],
	_xml_requests_map:[],
	_xml_http_objects:[],
	_xml_http_object_use:0,
	_xml_http_object_count:5,
	_xml_http_object_pool_size:5,
	_xml_http_object_pool_max:10,

	/* notate whether the pool was created */
	_xml_http_pool_created:0,
	/* notate whether the pool was created */
	_xml_http_pool_enabled:1,

	setCacheEnabled:function(b){
		org.cote.js.xml.clearCache();
		org.cote.js.xml._xml_http_cache_enabled = b;
	},

	getCacheEnabled:function(){
		return org.cote.js.xml._xml_http_cache_enabled;
	},
	
	setPoolEnabled:function(b){
		org.cote.js.xml._xml_http_pool_enabled = b;
	},

	getPoolEnabled:function(){
		return org.cote.js.xml._xml_http_pool_enabled;
	},


	getXmlHttpArray:function(){
		return org.cote.js.xml._xml_http_objects;
	},
		
	newXmlDocument:function(n){
		/*
			n = "root node";
		*/
		
		var r = 0,e;
		if(typeof document.implementation != "undefined" && typeof document.implementation.createDocument != "undefined"){
			r = document.implementation.createDocument("",n,null);
		}
		else if(typeof ActiveXObject != "undefined"){
			r = new ActiveXObject("MSXML.DOMDocument");
			e = r.createElement(n);
			r.appendChild(e);
		}
		else{
			/* ... */
		}
		return r;
	},

	/*
		2005/09/09 - better clear out the cached ids and arrays as well as nullifying the object pointers
	
	*/
	clearCache:function(){
		var _x = org.cote.js.xml,i = 0,o;
		for(;i<_x._xml_requests.length;i++){
			o = _x._xml_requests[i];
			if(o.cached && typeof o.cached_dom == "object"){
				o.cached_dom = 0;
			}
			o.obj = null;
			o.internal_handler = null;
			o.handler = null;
		}
		_x._xml_requests = [];
		_x._xml_requests_map = [];
	},
	resetXmlHttpObjectPool:function(){
		var _x = org.cote.js.xml,i = 0,o;
		_x._xml_http_pool_created = 1;
		_x._xml_http_object_use=0;
		_x._xml_http_objects=[];
		_x._xml_http_object_count = _x._xml_http_object_pool_size;
		for(;i < _x._xml_http_object_pool_size; i++)
			o = _x._xml_http_objects[i] = _x.newXmlHttpObject(1,i);
		
	},

	testXmlHttpObject:function(){
		return org.cote.js.xml.newXmlHttpObject(null,true,1);
	},

	newXmlHttpObject:function(b,i,z){
		/*
			b = return a hash for use with pooling
			i = pool index value.  b must be true for i to be used
			z = used for testing object creation
		*/
		var o = null,v,f;
		if(typeof XMLHttpRequest != "undefined"){
			o = new XMLHttpRequest();
			if(z) return 1;
		}
		else if(typeof ActiveXObject != "undefined"){
			try{
				o = new ActiveXObject("MSXML2.XMLHTTP.3.0");
				if(z) return 1;
			}
			catch(e){
				alert("XMLError: " + (e.description?e.description:e.message));
			}
			if(z) return 0;
		}
		if(b && typeof i == "number"){
			v= {
				xml_object:o,
				in_use:0,
				index:i,
				/* vid = variant id */
				vid:-1,
				handler:0
			};

			return v;
		}
		else{
			return o;
		}
		
	},

	returnXmlHttpObjectToPool:function(i, y){
		var _x = org.cote.js.xml,b=0,o,a;
		a = _x._xml_http_objects;

		if(typeof a[i] == "object"){
			o = a[i];
			if(o.index >= _x._xml_http_object_pool_size)
				a[i] = 0;
			

			try{
				if(!y){
					/* 2005/09/07 Fix for Opera 8 */
					/*
						Why bother checking the instance of?
						Either XMLHttpRequest is here and use it, or it's not so don't use it
					*/
					//if(typeof XMLHttpRequest == "function" || (typeof XMLHttpRequest == "object" &&  o.xml_object instanceof XMLHttpRequest))
					/* 2005/09/08 Fix for Safari */
					if(typeof XMLHttpRequest != "undefined"){
						if(typeof o.xml_object.removeEventListener == "function")
							o.xml_object.removeEventListener("load",o.handler,false);
						else
							o.xml_object.onreadystatechange = _x._stub;
					}
					else if(typeof ActiveXObject != "undefined" &&  o.xml_object instanceof ActiveXObject)
						o.xml_object.onreadystatechange=_x._stub;
					
					o.handler = 0;
				}
			}
			catch(e){
				alert("Error in returnXmlHttpObjectToPool: " + (e.description?e.description:e.message));
			}

			o.xml_object.abort();			
			o.in_use = 0;
			o.vid = -1;

			_x._xml_http_object_use--;
		}
		return 1;
	},
	
	getXmlHttpObjectFromPool:function(y){
		var _x = org.cote.js.xml,i = 0,b=0,o,a,n=-1,z=0;

		if(!_x._xml_http_pool_created) _x.resetXmlHttpObjectPool();
		a = _x._xml_http_objects;		
		for(;i<a.length;i++){
			if(typeof a[i] == "object" && typeof a[i].in_use == "number" && !a[i].in_use){
				a[i].in_use = 1;
				b = i;

				/* Mark that a valid index was located */
				z = 1;
				break;
			}
			/* mark the next known null marker for re-use*/
			/*
				06/18/2003: drop check for null in favor of absent value check !a[i]; this is in-line with current version 
			*/
			if(n == -1 && !a[i])
				n = i;
			
		}

		if(!z){
			b = (n > -1)?n:a.length;
			if(b < _x._xml_http_object_pool_max){
				a[b] = _x.newXmlHttpObject(1,b);
				a[b].in_use = 1;
			}
			else{
				//alert("Max pool size reached!");
				return null;
			}
		}

		if(b > -1){
			_x._xml_http_object_use++;
			o = a[b];
			try{
				if(!y){
					/* 2005/09/07 Fix f			}
			catch(e){
				alert("Error in getXmlHttpObjectFromPool: " + (e.description?e.description:e.message));
			}

			return a[b];
		}
		
		return null;

	},
	

	_handle_xml_request_load:function(xml_id){
		var _x=org.cote.js.xml,o,v,z;
		try{

			if(_x._xml_http_pool_enabled && typeof _x._xml_http_objects[xml_id] == "object"){
				z = _x._xml_http_objects[xml_id].vid;
				if(z == -1){
					alert("invalid  pool index for " + xml_id);
					return 0;
				}
				xml_id = z;
			}

			if(typeof _x._xml_requests_map[xml_id] == "number"){
				o = _x._xml_requests[_x._xml_requests_map[xml_id]];
				
				v = {"xdom":null,"id":(o.backup_id?o.backup_id:xml_id)};

				if(
					o.url.match(/^file:/i)
					&&
					typeof ActiveXObject != "undefined"
					&&
					o.o instanceof ActiveXObject
				){
					var mp = new ActiveXObject("MSXML.DOMDocument");
					mp.loadXML(o.o.responseText);
					v.xdom = mp;
				}
				else if(o.obj != null && o.obj.responseXML != null)
					v.xdom = o.obj.responseXML;
				
				else if(o.obj != null)
					alert("Error loading '" + o.url + "'. Re				
				else
					alert("Error loading '" + o.url + "'. The internal XML object reference is null");			
				
	
				}
		catch(e){
			alert("Error in handle_xml_request_load: " + (e.description?e.description:e.message));
		}
	},

	_handle_xml_request_readystatechange:function(xml_id){
		var _x=org.cote.js.xml,o;

		/*
		return org.cote.js.xml._request_xmlhttp(p,h,a,i,0,null,c);
	},

	/*
		postXml(path,data,handler,async,id);
		handler is optional for synchronous requests
		to specify a custom id for synchronous requests, use null or 0 for the handler, and false or 0 for the async property.
		id is optional.
		
		Note that postXml assumes no caching.
	*/
	postXml:function(p,d,h,			Caching is not provided for the postXml wrapper.
		*/
		return org.cote.js.xml._request_xmlhttp(p,h,a,i,1,d,0);
	},
	/*
		_		if(typeof d=="undefined") d = null;

		z = (x?"P		
		if(
			typeof _x._xml_requests_map[i] == "number"
			&&
			typeof _x._xml_requests[_x._xml_requests_map[i]] == "object"
		){
			r = _x._xml_requests				/* force disable caching for this request */
				c = 0;
				/* backup the id */
						if(!p.match(e)){
				m=location.pathname;
				m=m.substring(0,m.lastIndexOf("/")+1);
				p=m + p;
			}
			if(!location.protocol.match(/^file:$/i))
				p = location.protocol + "//" + location.host + p;
			else
				p = location.protocol + "//" + p;

		}
		_x._xml_requests[y].url = p;

		/*
			Add event handlers based on instance of XML object
			
			Must check for typeof object before inst				c) the type of request object
		*/
		b_ia = (typeof ActiveXObject != "undefined" &&  o instanceof ActiveXObject)?1:0;
		try{
			//if(!b && a && (typeof XMLHttpReques			}
			else if(!b && a && b_ia){
				_x._xml_requests[y].internal_handler = function(){org.cote.js.xml._handle_xml_request_readystatechange(i);};
				/*
					Can't attach an 		
		/*
			There is a problem with the ActiveXObject not liking the multiple levels of object references,
			particularly the embedded array
			/*
				Handle local file system requests
			*/
			if(
				p.match			if(!b && _x._xml_r			return n.xml;
		}
	},
	getCDATAValue:function(n){
		var c,d="",i=0,e;
		c = n.childNodes;
		for(;i<c.length;i++){
			e=c[i];
			if(e.nodeName=="#cdata-section") d+=e.nodeValue;
		}
		return d;
	},
	
	selectSingleNode:function(d,x,c){
		/*
			d = XmlDocument
			x = xpath
			c = context node
		*/
		var s,i,n;
		if(typeof d.evaluate != "undefined"){
			c = (c ? c : d.documentElement);
			s = d.evaluate(x,c,null,0,null);

			return s.iterateNext();
		}
		else if(typeof d.selectNodes != "undefined"){
			return (c ? c : d).selectSingleNode(x);
		}

		return 0;
	
	},
	selectNodes:function(d,x,c){
		/*
			d = X		}

		else if(typeof d.selectNodes != "undefined"){
			return (c ? c : d).selectNodes(x);
		}

		retur			 a = attribute name
			 v = attribute value
		*/

		var i=0,b,e,c,r=[];
		if(!z) r = null;
		
		c = x.getE				if(e.nodeType==3) r+=e.nodeValue;
				if(e.nodeType==1 && e.hasChildNodes()){
					r+=this.getInnerText(e);
				}
			}
		}
		return r;
	},
	removeChildren:function(o){
		var i;
		for(i=o.childNodes.length-1;i>=0;i--)
			o.removeChild(		/*
			t = target
			s = source
			p = preserve
			d = target document object
			z = no recur						e.style.cssText=v;
					}
					/* stupid IE */
					else if(b && n=="id"){
						e.id=v;
					}
					/* stupid IE */

					else if(b && n=="class"){
						e.className=v;
					}

					/* stupid IE */
					else if(b && n.match(/^						eval("e." + n + "=function(){" + v  +"}");
					}
					else{
						e.setAttribute(n,v);
					}
				}
	
				if(!z && s.hasChildNodes()){
					a=s.childNodes;
					l=a.length;
					for(x=0;x<l;x++){
						this.setInnerXHTML(e,a[x],1,d);
					}
				}
				t.appendChild(e);
				r = e;
				break;
			case 3:
						return o;

	}
};
