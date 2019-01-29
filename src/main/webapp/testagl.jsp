<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>
<!-- Angular应用绑定Dom根节点 -->
<html ng-app="myApp">
  <head>
	<script src="http://code.angularjs.org/1.2.25/angular.min.js"></script>
  </head>
  <body>
  	<!-- ng-controller为应用变量添加控制器  -->
  	<!-- ng-init初始化应用时创建一个变量 -->
    <div ng-controller="TestCtrl" ng-init="init()">
      <b>订单:</b>
      <div>
      		<!-- 绑定数据对象num与 Dom控件num-->
        	数量: <input name="num" type="number" ng-model="num" required >
      </div>
      <div>
      		<!-- 绑定数据对象price与 Dom控件price-->
        	单价: <input name="price" type="number" ng-model="price" required >
      </div>
      <div>
      	<!-- 插值中使用表单式-->
        <b>总价:{{ num * price}}</b> 
      </div>
    </div>
    <script>
    	//定义angular模块myApp,绑定到Dom根元素
	    var myApp = angular.module('myApp',[]);
	    //创建 'greeter' 服务 
	    myApp.factory('greeter', function($window) {
	        // 这是一个 factory 函数，负责创建 'greeter' 服务 
	        return {
	          greet: function(text) {
	            $window.alert(text);
	          }
	        };
		});
	    //在模块myApp中定义Controller
	    //$scope是数据作用域,负责数据对象管理
	    //TestCtrl注入了$scope与自定义服务greeter,注入了几个服务构造方法就需要几个参数function($scope,greeter)
	    myApp.controller('TestCtrl', ['$scope','greeter', function($scope,greeter) {
	    	//定义数据属性
	        $scope.num = 10;
	        $scope.price = 15;
	        //定义方法
	        $scope.init = function()
	        {
	        	greeter.greet('init function');
	        }
	    }]);

    </script>
  </body>
</html>