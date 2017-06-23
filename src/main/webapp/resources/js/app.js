var postManagerModule = angular.module('diary', []);

postManagerModule.controller('postController', function ($scope,$http) {
	alert('in app.js controller');
	 var urlBase="http://localhost:8080/Diary/posts";

	 $scope.toggle=true;

	 $scope.selection = [];

	 $scope.statuses=['ACTIVE','COMPLETED'];

	 $scope.priorities=['HIGH','LOW','MEDIUM'];

	 $http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded";

	 //get all tasks and display initially
	 alert('in app.js controller');
	 $http.get(urlBase+'/list-test').
     
	     success(function(data) {

	         $scope.posts = data;

	         for(var i=0;i<$scope.posts.length;i++){

	              $scope.selection.push($scope.posts[i].post_id);
	         }

	    });
});

