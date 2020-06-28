

var mainApp = new Vue({
    el: '#main-menu-app',
    template:`
    <div class="header_strip">
    <nav class="menu_container">
        <ul >
            <li><a :href="context + '/main'" class="menu_a"><i class="fa fa-home fa-lg"></i> Главная</a></li>
            <li><a href="#" class="menu_a" :class="{'not_clickable':checkAuthority('accounting_page')}"><i class="fa fa-calculator fa-lg"></i> Учет</a>
                <ul>
                    <li><a :href="context + '/accounting/remains'" class="menu_a" :class="{'not_clickable':checkAuthority('remains_page')}"><i class="fa fa-th"></i> Остатки товаров</a></li>
                    <li><a :href="context + '/accounting/incoming_docs'" class="menu_a" :class="{'not_clickable':checkAuthority('incoming_docs_page')}"><i class="fa fa-indent"></i> Приходные документы</a></li>
                    <li><a :href="context + '/accounting/sku_in'" class="menu_a" :class="{'not_clickable':checkAuthority('sku_in_page')}"><i class="fa fa-arrow-right"></i> Приход товаров</a></li>
                    <li><a :href="context + '/accounting/outgoing_docs'" class="menu_a" :class="{'not_clickable':checkAuthority('outgoing_docs_page')}"><i class="fa fa-outdent"></i> Расходные документы</a></li>
                    <li><a :href="context + '/accounting/sku_out'" class="menu_a" :class="{'not_clickable':checkAuthority('sku_out_page')}"><i class="fa fa-arrow-left"></i> Расход товаров</a></li>
                </ul>
            </li>
            <li><a :href="context + '/sku'" class="menu_a" :class="{'not_clickable':checkAuthority('sku_page')}"><i class="fa fa-shopping-basket fa-lg"></i> Товары</a></li>
            <li><a :href="context + '/contractors'" class="menu_a" :class="{'not_clickable':checkAuthority('contractors_page')}"><i class="fa fa-handshake-o fa-lg"></i> Контрагенты</a></li>           
            <li><a href="#" class="menu_a" :class="{'not_clickable':checkAuthority('admin_page')}"><i class="fa fa-gears fa-lg"></i> Администрирование</a>
                <ul>
                    <li><a :href="context + '/admin/users'" class="menu_a" :class="{'not_clickable':checkAuthority('users_page')}"><i class="fa fa-group"></i> Пользователи</a></li>
                    <li><a :href="context + '/admin/positions'" class="menu_a" :class="{'not_clickable':checkAuthority('positions_page')}"><i class="fa fa-hand-o-right"></i> Должности</a></li>
                </ul>
            </li>
            <li><a href="#" class="menu_a" id="authInfoUserBlock">{{showAuthInfoUser}}<br>{{showAuthInfoPosition}}</a>
                <ul>
                   <li ><a href="#" class="menu_a">Version: 1.0.0<br>Developed by:<br>Ruben Khodzhaev<br>Copyright 2020 ©</a></li>
                   <li ><a :href="context + '/logout'" class="menu_a"><i class="fa fa-sign-out"></i> Выход</a></li>
                </ul>
            </li>

        </ul>
    </nav>
</div>
    
    `,
    data() {
        return {
            currentUserProfile:authData.profile,
            currentUser:{
               name: authData.name,
               surname: authData.surname,
               position: authData.position
            },
            context:contextPath

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