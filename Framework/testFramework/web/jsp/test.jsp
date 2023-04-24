<%@ page import="test.* , java.util.ArrayList"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <% if (request.getAttribute("lst")!=null) {  %>
        <% try { ArrayList<Test> all = (ArrayList<Test>) request.getAttribute("lst") ;%>
    <% for (Test a : all)  { %>
        <%= a.getName()%>
    <% }} catch(Exception exe )  { %>
        <%= exe.getMessage()%>
    <%}}else{%>
        mofo
    <%}%>
    <% if(request.getAttribute("erreur")!=null) { %>
        <%= (String)request.getAttribute("erreur") %>
    <%}%>
</body>
</html>