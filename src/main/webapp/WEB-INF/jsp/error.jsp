<%-- 
    Document   : accessdenied.jsp
    Created on : 2019. 10. 31, 오후 2:54:49
    Author     : Dev
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width">
    <link rel="stylesheet" href="${servletPath}/js/map/bootstrap@4.6/css/bootstrap.min.css" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">
    <script src="${servletPath}/js/map/bootstrap@4.6/js/bootstrap.bundle.min.js" integrity="sha384-alpBpkh1PFOepccYVYDB4do5UnbKysX5WZXm3XxPqe5iKTfUKjNkCk9SaVuEZflJ" crossorigin="anonymous"></script>
    
    <style type="text/css">
    body{
	  margin-top: 150px;
		background-color: #C4CCD9;
	}
	.error-main{
	  background-color: #fff;
	  box-shadow: 0px 10px 10px -10px #5D6572;
	}
	.error-main h1{
	  font-weight: bold;
	  color: #444444;
	  font-size: 100px;
	  text-shadow: 2px 4px 5px #6E6E6E;
	}
	.error-main h6{
	  color: #42494F;
	}
	.error-main p{
	  color: #9897A0;
	  font-size: 14px; 
	}
    </style>
</head>
<body>
    <div class="container">
      <div class="row text-center">
        <div class="col-lg-6 offset-lg-3 col-sm-6 offset-sm-3 col-12 p-3 error-main">
          <div class="row">
            <div class="col-lg-8 col-12 col-sm-10 offset-lg-2 offset-sm-1">
              <h1 class="m-0">404</h1>
              <h6>That’s an error.</h6>
              <p>
	              The requested URL was <span class="text-info">not found</span> on this server. 
	              <span class="text-info"><br />That’s all we know.</span>
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
</body>
</html>
