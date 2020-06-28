

var mainApp = new Vue({
    el: '#main-app',
    data() {
        return {
            currentUserProfile:authData.profile,
            currentUser:{
               name: authData.name,
               surname: authData.surname,
               position: authData.position
            },

        }
    },
    methods: {
        checkAuthority(auth) {
            var result = true;
            var currentUserAuth = this.currentUserProfile.authorities;
            for (var i = 0; i < currentUserAuth.length; i++) {
                if (currentUserAuth[i].authority === auth) {
                    result = false;
                    break;
                }
            }
            return result;
        }

    },
    computed: {
        showAuthInfoUser() {
            return this.currentUser.surname + " " + this.currentUser.name;
        },
        showAuthInfoPosition() {
            return this.currentUser.position;
        },

    },

});