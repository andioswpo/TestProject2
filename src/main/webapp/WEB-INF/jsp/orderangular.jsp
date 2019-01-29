<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>列表</title>
<link rel="stylesheet" type="text/css" href="/static/js/base.css">
<script type="text/javascript" src="/static/js/jquery.min.js"></script>
<script type="text/javascript" src="/static/js/base.js"></script>
<script src="https://cdn.staticfile.org/angular.js/1.4.6/angular.min.js"></script>
<body ng-app="spicyApp1" ng-controller="SpicyCtrl" ng-init="loadorderlist()">
	<div >
		<input type="button" value="添加" ng-click="doadd()"/>
		<table>
			<tr>
				<th>全选</th>
				<th>名称</th>
				<th>时间</th>
				<th>金额</th>
				<th>省份</th>
				<th>地区</th>
				<th>县市</th>
			</tr>
			<tr ng-repeat="o in orderlist" ng-click="openorder(o.id)">
				<td>{{o.id}}</td>
				<td>{{o.name}}</td>
				<td>{{o.ts}}</td>
				<td>{{o.amount}}</td>
				<td>{{o.provinceName}}</td>
				<td>{{o.cityName}}</td>
				<td>{{o.countyName}}</td>
			</tr>
		</table>
	</div>
	
	<div id="orderadd" style="display:{{showorderdiv}}">
		<table>
			<tr>
				<td colspan="2">
					订单主表
					<input type="button" value="关闭" ng-click="doclose()"/>
					<input type="button" value="添加订单明细" ng-click="additem()"/>
					<input type="button" value="保存订单" ng-click="saveorder()"/>
				</td>
			</tr>
			<tr>
				<td>名称</td>
				<td><input type="text" ng-model="order.name"/></td>
			</tr>
			<tr>
				<td>时间</td>
				<td><input type="text" ng-model="order.ts"/></td>
			</tr>
			<tr>
				<td>金额</td>
				<td><input type="text" ng-model="order.amount"/></td>
			</tr>
			<tr>
				<td>省份</td>
				<td>
					<select ng-model="order.provinceCode" ng-change="selectcity()"
						ng-options="p.code as p.name for p in provicelist"></select>
				</td>
			</tr>
			<tr>
				<td>地区</td>
				<td>
					<select ng-model="order.cityCode" ng-change="selectcounty()"
						ng-options="c.code as c.name for c in citylist"></select>
				</td>
			</tr>
			<tr>
				<td>县市</td>
				<td>
					<select ng-model="order.countyCode" 
						ng-options="c.code as c.name for c in countylist"></select>
				</td>
			</tr>
		</table>
		<!-- 订单明细  -->
		<table>
			<tr>
				<td colspan="10">
					订单明细
				</td>
			</tr>
			<tr>
				<th>主键</th>
				<th>产品</th>
				<th>数量</th>
				<th>价格</th>
				<th>金额</th>
				<th>操作</th>
			</tr>
			<tr ng-repeat="oi in order.items">
				<td>{{oi.id}} {{oi.uuid}}</td>
				<td>
					<select ng-model="oi.prdtId" 
						ng-options="c.code as c.name for c in prdtlist"></select>
				</td>
				<td><input type="text" ng-model="oi.num"/></td>
				<td><input type="text" ng-model="oi.price"/></td>
				<td>{{oi.num*oi.price}}</td>
				<td><button ng-click="dodelete(oi.uuid)">删除</button></td>
			</tr>
		</table>
	</div>
	
	<script>
		var myApp = angular.module('spicyApp1', []);
		
		myApp.controller('SpicyCtrl', ['$scope','$http', function($scope,$http){
		    $scope.showorderdiv = 'none';
		    $scope.order = {};
		    $scope.prdtlist = [{code:'1',name:'手机'},{code:'2',name:'笔记本'},{code:'3',name:'数码相机'}];
		    
		    $scope.loadorderlist = function() {
	    	      $http({method: 'GET',url:'/orderlist.action'}).
	    	        then(function(response) {
	    	          $scope.orderlist = response.data.rows;
	    	        }, function(response) {
	    	          $scope.data = response.data || 'Request failed';
	    	      });
		    };
		    $scope.doadd = function() {
		    	$scope.showorderdiv = 'block';
		    	$scope.loadaddress('','','');
		    };
		    $scope.dodelete = function(uuid) {
				var len = $scope.order.items.length;
				for(var i=0;i<len;i++)
				{
					if($scope.order.items[i].uuid == uuid)
					{
						$scope.order.items.splice(i,1);
					}
				}
		    };
		    $scope.doclose = function() {
		    	$scope.showorderdiv = 'none';
		    };
		    $scope.additem = function() {
				$scope.order.items.push({
					id:'',
					prdtId:'1',
					num:1,
					price:1,
					uuid:Math.random()
				});
			};
			$scope.saveorder = function()
			{
				$http({method: 'POST',url:'/ordersave.action',data:$scope.order})
				  .then(function (response) {
					  $scope.loadorderlist();
					  $scope.order = response.data;
				});
			};
		    $scope.openorder = function(id)
		    {
		    	$scope.showorderdiv = 'block';
	    	      $http({method: 'GET',url:'/loadorder.action',params:{id:id}}).
	    	        then(function(response) {
	    	          $scope.order = response.data;
	    	          $scope.loadaddress($scope.order.provinceCode,
	    	        		  $scope.order.cotuCode,$scope.order.countyCode);
	    	        }, function(response) {
	    	          $scope.data = response.data || 'Request failed';
	    	      });
		    };
		    $scope.selectcity = function(){
  	          //装载地区
  	          var province = $scope.order.provinceCode;
	    	    $http({method: 'GET',url:'/city.action',params:{pcode:province}})
	    	    //加载地区列表成功处理代码 
  	          	.then(function(response) {
	    	          $scope.citylist = response.data;
	    	          $scope.order.cityCode = $scope.citylist[0].code;
	    	          var city = $scope.citylist[0].code;
	    	          //装载县市
		    	      $http({method: 'GET',url:'/county.action',params:{city:city}})
		    	    	//加载地区列表成功处理代码 
	    	          	.then(function(response) {
		    	          $scope.countylist = response.data;
		    	          $scope.order.countyCode = $scope.countylist[0].code;
  	          	  		});
	    	          
	    	   });
		    };
		    $scope.selectcounty = function(){
  	          var city = $scope.order.cityCode;
   	          //装载县市
    	      $http({method: 'GET',url:'/county.action',params:{city:city}})
    	    	//加载地区列表成功处理代码 
   	          	.then(function(response) {
    	          $scope.countylist = response.data;
    	          $scope.order.countyCode = $scope.countylist[0].code;
          	  	});
		    };
		    $scope.loadaddress =function(province,city,county)
			{
	    	      $http({method: 'GET',url:'/province.action'})
	    	      	//加载身份列表成功处理代码 
	    	      	.then(function(response) {
	    	          $scope.provicelist = response.data;
	    	          if(province=='' || province == null)
	    	          {
	    	          	$scope.order.provinceCode = $scope.provicelist[0].code;
	    	          	province = $scope.provicelist[0].code;
	    	          }
	    	          //装载地区
		    	      $http({method: 'GET',url:'/city.action',params:{pcode:province}})
		    	    	//加载地区列表成功处理代码 
	    	          	.then(function(response) {
		    	          $scope.citylist = response.data;
		    	          if(city=='' || city == null)
		    	          {
		    	          	$scope.order.cityCode = $scope.citylist[0].code;
		    	          	city = $scope.citylist[0].code;
		    	          }
		    	          //装载县市
			    	      $http({method: 'GET',url:'/county.action',params:{city:city}})
			    	    	//加载地区列表成功处理代码 
		    	          	.then(function(response) {
			    	          $scope.countylist = response.data;
			    	          if(county=='' || county == null)
			    	          {
			    	          	$scope.order.countyCode = $scope.countylist[0].code;
			    	          	city = $scope.countylist[0].code;
			    	          }
	    	          	  });
		    	          
		    	      });
	    	          
	    	        //加载身份列表失败处理代码 
	    	        }, function(response) {
	    	          
	    	      });
	
			}
		}]);
	</script>
</body>
</html>