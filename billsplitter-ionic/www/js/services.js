angular.module('starter.services', [])


.factory('Bills', function() {
    var bills = [{
        id: 0,
        payee: 'Ben Sparrow',
        amount: '$ 5,20',
        description: 'Coffee at Wayne\'s',
        face: 'img/ben.png'
    }, {
        id: 1,
        payee: 'Mike Harrington',
        amount: '$ 15,40',
        description: 'Dinner at Vigarda',
        face: 'img/mike.png'
    }];

    var participants = [{
        id: 0,
        name: 'Ben Sparrow',
        face: 'img/ben.png',
        balance: -27
    }, {
        id: 1,
        name: 'Mike Harrington',
        face: 'img/mike.png',
        balance: -53
    }, {
        id: 2,
        name: 'Adam Doe',
        face: 'img/adam.jpg',
        balance: 80
    }];

    return {
        all: function() {
            return bills;
        },
        allParticipants: function() {
          return participants;
        },
        remove: function(bill) {
            bills.splice(bills.indexOf(bill), 1);
        },
        get: function(billId) {
            for (var i = 0; i < bills.length; i++) {
                if (bills[i].id === parseInt(billId)) {
                    return bills[i];
                }
            }
            return null;
        },
        addBill: function(bill) {
            bills.push({
                payee: 'Adam Doe',
                amount: bill.amount,
                description: bill.description,
                face: 'img/adam.png'
            })
        }
    }
});
