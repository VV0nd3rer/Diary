var signupApp = angular.module('signup', ['ngMaterial','ngMessages']);

signupApp.controller('userController',

	function($scope, $http) {

	$scope.addUser = function addUser() {
		console.log('username: ' + $scope.username);
		console.log('form.username' + $scope.signupForm.username);
		var user = {
				username:$scope.username,
				email:$scope.email,
				password:$scope.password
		};

		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		$(document).ajaxSend(function(e, xhr, options) {
			xhr.setRequestHeader(header, token);
		});
		var success_url = '/users/login';
		var error_url = '/transaction-error';

		$scope.isLoading = true;

		$http.post('/users/add-user',  JSON.stringify(user))
			.then(function(response) {
				$scope.isLoading = false;
				console.log(JSON.stringify(response));
				angular.element('#signupForm').remove();
				angular.element('#signupResult').replaceWith(response.data);
			});/*function successCallback(response) {
				$scope.isLoading = false;
				console.log(JSON.stringify(response));
				angular.element('#signupForm').remove();
				angular.element('#signupResult').replaceWith(response.data);
		}, function errorCallback(response) {
				$scope.isLoading = false;
				angular.element('#signupForm').remove();
				angular.element('#signupResult').replaceWith(response.data);
		});*/
	}
});
signupApp.directive('usernameAvailable',function($timeout, $q) {
	return {
		restrict: 'AE',
		require: 'ngModel',
		link: function(scope, elm, attr, model) {
			model.$asyncValidators.usernameExists = function(username) {
				/*var token = $("meta[name='_csrf']").attr("content");
				var header = $("meta[name='_csrf_header']").attr("content");
				$(document).ajaxSend(function(e, xhr, options) {
					xhr.setRequestHeader(header, token);
				});*/
				var username_existing_url = "/users/username-exists/" + username;
				var isValid = false;
				var defer = $q.defer();
				$.get(username_existing_url, function(usernameExists) {
					isValid = !usernameExists;
					$timeout(function() {
						model.$setValidity('usernameExists', isValid);
						defer.resolve;
					}, 1000);
					scope.$apply(function(){
						scope.username = username;
					});
				});
				return defer.promise;
			};
		}
	}
});
signupApp.directive('emailAvailable',function($timeout, $q) {
	return {
		restrict: 'AE',
		require: 'ngModel',
		link: function(scope, elm, attr, model) {
			model.$asyncValidators.emailExists = function(email) {
				var email_existing_url = "/users/email-exists/";
				var isValid = false;
				var defer = $q.defer();

				var token = $("meta[name='_csrf']").attr("content");
				var header = $("meta[name='_csrf_header']").attr("content");
				$(document).ajaxSend(function(e, xhr, options) {
					xhr.setRequestHeader(header, token);
				});

				$.ajax({
						type: "POST",
						url:  email_existing_url,
						data: email,
                        /*contentType When sending data to the server, use this content type.
						  dataType The type of data that you're expecting back from the server.*/
						contentType: "text/plain",
						error: function(xhr, statusText, errorThrown) { alert("Error: "+statusText + ", " + errorThrown); },
						success: function(emailExists){
							$timeout(function() {
								isValid = !emailExists;
								model.$setValidity('emailExists', isValid);
								defer.resolve;
							}, 1000);
							scope.$apply(function(){
								scope.email = email;
							});
						}
					}
				);
				return defer.promise;
			};
		}
	}
});
signupApp.directive('passwordVerify', function() {
	return {
		restrict: 'A', // only activate on element attribute
		require: '?ngModel', // get a hold of NgModelController
		link: function(scope, elem, attrs, ngModel) {
			if (!ngModel) return; // do nothing if no ng-model

			// watch own value and re-validate on change
			scope.$watch(attrs.ngModel, function() {
				validate();
			});

			// observe the other value and re-validate on change
			attrs.$observe('passwordVerify', function(val) {
				validate();
			});

			var validate = function() {
				// values
				var val1 = ngModel.$viewValue;
				var val2 = attrs.passwordVerify;

				// set validity
				ngModel.$setValidity('passwordVerify', val1 === val2);
			};
		}
	}
});