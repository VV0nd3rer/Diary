//Common module
angular.module('passwordVerifyModule', ['ngMaterial','ngMessages']).directive('passwordVerify', function() {
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
var signupApp = angular.module('signup', ['ngMaterial','ngMessages','passwordVerifyModule']);
var loginApp = angular.module('login', ['ngRoute','ngMaterial','ngMessages']);
var forgotPassApp = angular.module('forgotPass', ['ngMaterial','ngMessages']);
var resetPassApp = angular.module('resetPass', ['ngMaterial','ngMessages','passwordVerifyModule']);
var searchApp = angular.module('search', ['ngMaterial','ngMessages']);
var profileApp = angular.module('profile', ['ngMaterial', 'ngMessages','passwordVerifyModule']);

profileApp.controller('UserMenuCtrl', function($scope){

		$scope.currentNavItem = 'info';

		$scope.goto = function (page) {
			console.log("Goto " + page);
			console.log("$scope.currentNavItem " + $scope.currentNavItem);
			var profileUsername = $("#profile-username").text();
			console.log('profile username ' + profileUsername);
			if(page == 'info') {
				$.get('/users/user-info/'+profileUsername, function(modelAndView) {
					$("#nav-bar-content").html(modelAndView);
				});
			}
			if(page == 'statistic') {
				$.get('/users/user-statistic', function(modelAndView) {
					$("#nav-bar-content").html(modelAndView);
				});
			}
			if(page == 'favorite') {
				$.get('/users/user-favorite/'+profileUsername, function(modelAndView) {
					$("#nav-bar-content").html(modelAndView);
				});
			}
		}
	angular.element('#pass-modal-form-cancel-btn').click(function() {
		console.log("pressed cancel btn");
		$("#pass-modal-form").modal("hide");
		$(':input','#pass-modal-form')
			.not(':button, :submit, :reset, :hidden')
			.val('');
		$('.errorMessage').hide();
	});
	$scope.submit = function(){
		console.log("form submitted");
		$scope.submitted = true;
	}
	angular.element('#modal-form-save-pass-btn').click(function() {
		var data = {};
		var current_pass = $("#current_password").val();
		var password = $("#password").val();
		var confirm_password = $("#confirm_password").val();
		data['currentPassword'] = current_pass;
		data['password'] = password;
		data['matchingPassword'] = confirm_password;
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		$(document).ajaxSend(function(e, xhr, options) {
			xhr.setRequestHeader(header, token);
		});

		var upd_pass_url = "/users/update-password-in-session/";
		$.ajax({
			url: upd_pass_url,
			type:"POST",
			contentType:"application/json; charset=utf-8",
			data: JSON.stringify(data),
			success: function(res){
				if(res == 'OK') {
					$("#pass-modal-form").modal("hide");
				} else  if(res == 'ERROR') {
					$("#error").show();
				} else if(res == 'NEW_PASSWORD_IS_THE_SAME') {
					$("#newPassAsOldError").show();
				} else if(res == 'NEW_PASSWORD_MISMATCHED') {
					$("#passMismatchingError").show();
				} else {
					$("#transactionError").show();
				}

			},
			error : function(e) {
				console.log("Error: ", e);
			},
			done : function(e) {
				alert("DONE");
			}
		});
	});
});

loginApp.controller('userController', function($scope, $window, $timeout, $q) {
	angular.element('#login-btn').click(function() {
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		$(document).ajaxSend(function(e, xhr, options) {
			xhr.setRequestHeader(header, token);
		});
		var defer = $q.defer();
		$.ajax({
			url: '/login',
			type:"POST",
			data: $("#loginForm").serialize(),
			dataType:"json",
			success: function(res){
				$window.location.href= '/posts';

			}, error : function(e) {
				$timeout(function() {
					$scope.loginForm.$setValidity("loginFailed", false);
					defer.resolve;
				}, 1000);
			}
		});
	});
});
forgotPassApp.controller('userController', function($scope) {

});
resetPassApp.controller('userController', function($scope) {

});
signupApp.controller('userController',

	function($scope, $http) {

	$scope.addUser = function addUser() {
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

/*searchApp.directive('inputVerify', function($timeout, $q){
	return {
		restrict: 'AE',
		require: 'ngModel',
		link: function(scope, elm, attr, model) {
			console.log("calling directive... ");
			model.$asyncValidators.inputFromList = function(catSuggestInput) {
				console.log("calling directive... ");
				var isValid = false;
				var defer = $q.defer();
				var listId = attr.list;
				var options = $('#' + listId + ' option');
				for (var i = 0; i < options.length; i++) {
					var option = options[i];
					if (option.innerText === catSuggestInput) {
						isValid = true;
						break;
					}
				}
				$timeout(function() {
					model.$setValidity('inputFromList', isValid);
					defer.resolve;
				}, 1000);

				return defer.promise;
			};
		}
	}
});*/
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
/*signupApp.directive('passwordVerify', function() {
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
});*/

loginApp.directive('loginResult',function() {
	return {
		restrict: 'A',
		link: function (scope, element, attrs) {
			scope.$watch(attrs.ngModel, function () {
				scope.loginForm.$setValidity("loginFailed", true);
			});
		}
	}
});