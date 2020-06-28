    var users_filter_comp = Vue.component('app-filter-users', {
        template: `
     <tr class="main_table_tr">
              <td class="filter_cell_td">
              <input class="input_filter"  v-model="params.login" :class="{'input-is-not-empty-style': params.login !== ''}" @keyup.enter="sendData()"></td> 
                <td class="filter_cell_td">
                <input class="input_filter" v-model="params.surnameANDname" :class="{'input-is-not-empty-style': params.surnameANDname !== ''}" @keyup.enter="sendData()"></td>
                <td class="filter_cell_td">
                <input class="input_filter"  v-model="params.position$position" :class="{'input-is-not-empty-style': params.position$position !== ''}" @keyup.enter="sendData()"></td>                                
                <td class="filter_cell_td">
                <input class="input_filter"   v-model="params._boolean_isActive" :class="{'input-is-not-empty-style': params._boolean_isActive !== ''}" @keyup.enter="sendData()"></td>                
                <td class="filter_cell_td">
                <input class="input_filter" v-model="params._date_createDate" :class="{'input-is-not-empty-style': params._date_createDate !== ''}" @keyup.enter="sendData()"></td>
                <td class="filter_cell_td">
                <input class="input_filter"  v-model="params._date_lastUpdateDate" :class="{'input-is-not-empty-style': params._date_lastUpdateDate !== ''}" @keyup.enter="sendData()"></td>
                <td class="filter_cell_td"></td>
                <td class="filter_cell_td"></td>
            </tr>
    
    `,
        data() {
            return {
                params: {
                    login: '',
                    surnameANDname: '',
                    position$position: '',
                    _boolean_isActive: '',
                    _date_createDate: '',
                    _date_lastUpdateDate: '',
                    columnToSort: '',
                    directionToSort: ''
                }
            }
        },
        methods: {
            sendData() {
                usersApp.listOfUsers = [];
                usersControllerGetFiltered.save(this.params).then(result => result.json().then(data => data.forEach(user => {
                    usersApp.listOfUsers.push(user);

                    }
                )))
            },
        },
    });
