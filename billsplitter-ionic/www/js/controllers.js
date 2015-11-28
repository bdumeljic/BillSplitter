angular.module('starter.controllers', [])

.controller('DashCtrl', function($scope, Bills) {
  $scope.participants = Bills.allParticipants();
})

.controller('AccountCtrl', function($scope) {
  $scope.settings = {
    enableFriends: true
  };
})

.controller('BillsCtrl', function($scope, Bills) {
  $scope.bills = Bills.all();
  $scope.remove = function(bill) {
    Bills.remove(bill);
  }
})

.controller('BillDetailCtrl', function($scope, $stateParams, Bills) {
  $scope.bill = Bills.get($stateParams.billId);
})

.controller('AddBillCtrl', function($scope) {

});
