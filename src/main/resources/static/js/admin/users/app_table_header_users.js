Vue.component('app-table-header-users', {
    template: `
     <tr class="main_table_tr">
                <th class="column_1"><a href="#" class="menu_title_link" @click="sort('login')">Логин<div :class="sortIconSelector('login')"></div></a></th>
                <th class="column_2"><a href="#" class="menu_title_link" @click="sort('fullname')">Фамилия и имя<div :class="sortIconSelector('fullname')"></div></a></th>
                <th class="column_3"><a href="#" class="menu_title_link" @click="sort('position')">Должность<div :class="sortIconSelector('position')"></div></a></th>
                <th class="column_4"><a href="#" class="menu_title_link" @click="sort('active')">Акт.<div :class="sortIconSelector('active')"></div></a></th>
                <th class="column_5"><a href="#" class="menu_title_link" @click="sort('createDate')">Дата создания<div :class="sortIconSelector('createDate')"></div></a></th>
                <th class="column_6"><a href="#" class="menu_title_link" @click="sort('updateDate')">Дата изменения<div :class="sortIconSelector('updateDate')"></div></a></th>
                <th class="column_7">Обновить</th>
                <th class="column_8">Удалить</th>
            </tr>    
    `,
    data() {
        return {
            currentSort:'',
            currentSortDir:''
        }
    },
    methods: {
        sort(columnName) {

            if(columnName === this.currentSort) {
                this.currentSortDir = this.currentSortDir==='asc'?'desc':'asc';
            } else {
                this.currentSortDir =  'asc';
            }
            this.currentSort = columnName;
            var sorted = usersApp.listOfUsers.sort(this.comparator);
            usersApp.listOfUsers = sorted;
        },
        comparator(o1, o2) {
            var result = 0;
            if(this.currentSort === 'login'){
                if(this.currentSortDir === 'asc'){
                    result = this.compareStringsAsc(o1.login, o2.login)
                } else {
                    result = this.compareStringsDesc(o1.login, o2.login)
                }
            } else if(this.currentSort === 'fullname') {
                if(this.currentSortDir === 'asc'){
                    result = this.compareStringsAsc(o1.surname, o2.surname);
                    if (result === 0) {
                        result = this.compareStringsAsc(o1.name, o2.name);
                    }
                } else {
                    result = this.compareStringsDesc(o1.surname, o2.surname)
                    if (result === 0) {
                        result = this.compareStringsDesc(o1.name, o2.name);
                    }
                }
            } else if(this.currentSort === 'position') {
                if(this.currentSortDir === 'asc'){
                    result = this.compareStringsAsc(o1.position.position, o2.position.position)
                } else {
                    result = this.compareStringsDesc(o1.position.position, o2.position.position)
                }
            } else if(this.currentSort === 'active') {
                var active1 = o1.active ? "Да" : "Нет";
                var active2 = o2.active ? "Да" : "Нет";
                if(this.currentSortDir === 'asc'){
                    result = this.compareStringsAsc(active1, active2)
                } else {
                    result = this.compareStringsDesc(active1, active2)
                }
            } else if(this.currentSort === 'createDate') {
                var date1 = new Date(o1.createDate);
                var date2 = new Date(o2.createDate);
                if(this.currentSortDir === 'asc'){
                    result = this.compareDatesAsc(date1, date2)
                } else {
                    result = this.compareDatesDesc(date1, date2)
                }
            } else if(this.currentSort === 'updateDate') {
                var date1 = new Date(o1.lastUpdateDate);
                var date2 = new Date(o2.lastUpdateDate);
                if(this.currentSortDir === 'asc'){
                    result = this.compareDatesAsc(date1, date2)
                } else {
                    result = this.compareDatesDesc(date1, date2)
                }
            }
            return result;
        },
        compareStringsAsc(string1, string2) {
            string1 = string1.toLowerCase();
            string2 = string2.toLowerCase();
            var result = 0;
            if(string1 < string2) {
                result = -1;
            }
            if(string1 > string2) {
                result = 1;
            }
            return result;
        },
        compareStringsDesc(string1, string2) {
            string1 = string1.toLowerCase();
            string2 = string2.toLowerCase();
            var result = 0;
            if(string1 < string2) {
                result = 1;
            }
            if(string1 > string2) {
                result = -1;
            }
            return result;
        },
        compareDatesAsc(date1, date2) {
            var result = 0;
            if(date1 < date2) {
                result = -1;
            }
            if(date1 > date2) {
                result = 1;
            }
            return result;
        },
        compareDatesDesc(date1, date2) {
            var result = 0;
            if(date1 < date2) {
                result = 1;
            }
            if(date1 > date2) {
                result = -1;
            }
            return result;
        },
        sortIconSelector(fieldName) {
            var result = "arrow_icon_neutral fa fa-sort";
            if (this.currentSortDir === 'asc' && this.currentSort === fieldName) {
                result = "arrow_icon_up fa fa-sort-asc";
            } else if (this.currentSortDir === 'desc' && this.currentSort === fieldName) {
                result = "arrow_icon_down fa fa-sort-desc";
            }
            return result;
        }
    },

});