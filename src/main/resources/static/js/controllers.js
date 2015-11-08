
angular.module('dropzone', []).directive('dropzone', function () {
  return function (scope, element, attrs) {
    var config, dropzone;

    config = scope[attrs.dropzone];

    dropzone = new Dropzone(element[0], config.options);

    angular.forEach(config.eventHandlers, function (handler, event) {
      dropzone.on(event, handler);
    });
  };
});

var uploadApp = angular.module('UploadApp', ['ngRoute', 'dropzone']);


uploadApp.config(['$routeProvider', function($routeProvider) {
	$routeProvider.
		when('/', {
			controller: 'ListController',
			templateUrl: 'list.html'
		}).
		when('/detail/:id', {
			controller: 'DetailController',
			templateUrl: 'detail.html'
		}).
		otherwise({
			redirectTo: '/'
		});
}]);


uploadApp.controller('UploadController', ['$scope', '$route', function($scope, $route) {
	
	$scope.dropzoneConfig = {
		    options: { 
		      url: '/uploads',
		      createImageThumbnails: false,
		      previewTemplate : '<div style="display:none"></div>'
		    },
		    'eventHandlers': {
		      'sending': function (file, xhr, formData) {
		      },
		      'success': function (file, response) {
		    	  $route.reload();
		      }
		    }
		  };
}]);

uploadApp.controller('ListController', ['$scope', '$http', function($scope, $http) {
	$scope.loadData = function () {
		$http.get('/uploads').success(function(data, status, headers, config) {
			$scope.items = data;
		});	
	}
	
	$scope.loadData();
		
}]);


uploadApp.controller('DetailController', ['$scope', '$routeParams', '$http', function($scope, $routeParams, $http) {
	$scope.loadData = function () {
		$http.get('/uploads/' + $routeParams.id).success(function(data, status, headers, config) {
			$scope.command = data;
		});
	}
	
	$scope.loadData();
}]);

