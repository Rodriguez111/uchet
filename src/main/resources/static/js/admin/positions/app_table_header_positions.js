Vue.component('app-table-header-positions', {
    template: `
     <tr class="main_table_tr">
                <th class="column_1"><a href="#" class="menu_title_link" @click="sort('position')">Должность<div :class="sortIconSelector('position')"></div></a></th>
                <th class="column_2"><a href="#" class="menu_title_link" @click="sort('description')">Описание должности<div :class="sortIconSelector('description')"></div></a></th>                
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
            var sorted = positionsApp.listOfPositions.sort(this.comparator);
            positionsApp.listOfPositions = sorted;
        },
        comparator(o1, o2) {
            var result = 0;
            if(this.currentSort === 'position'){
                if(this.currentSortDir === 'asc'){
                    result = this.compareStringsAsc(o1.position, o2.position)
                } else {
                    result = this.compareStringsDesc(o1.position, o2.position)
                }
            } else if(this.currentSort === 'description') {
                if(this.currentSortDir === 'asc'){
                    result = this.compareStringsAsc(o1.positionDescription, o2.positionDescription)
                } else {
                    result = this.compareStringsDesc(o1.positionDescription, o2.positionDescription)
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