var table_header_comp = Vue.component('app-table-header-sku', {
    template: `
     <tr class="main_table_tr">
                <th class="column_1"><a href="#" class="menu_title_link" @click="sort('code')">Код<div :class="sortIconSelector('code')"></div></a></th>
                <th class="column_2"><a href="#" class="menu_title_link" @click="sort('name')">Наименование<div :class="sortIconSelector('name')"></div></a></th>
                <th class="column_3"><a href="#" class="menu_title_link" @click="sort('contractor')">Поставщик<div :class="sortIconSelector('contractor')"></div></a></th>
                <th class="column_4"><a href="#" class="menu_title_link" @click="sort('unit')">Ед. изм.<div :class="sortIconSelector('unit')"></div></a></th>
                <th class="column_5"><a href="#" class="menu_title_link" @click="sort('pkg')">Втор. уп.<div :class="sortIconSelector('pkg')"></div></a></th>
                <th class="column_6"><a href="#" class="menu_title_link" @click="sort('active')">Акт.<div :class="sortIconSelector('active')"></div></a></th>
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
            var sorted = skuApp.listOfSku.sort(this.comparator);
            skuApp.listOfSku = sorted;
        },
        comparator(o1, o2) {
            var result = 0;
            if(this.currentSort === 'code'){
                if(this.currentSortDir === 'asc'){
                    result = this.compareStringsAsc(o1.code, o2.code)
                } else {
                    result = this.compareStringsDesc(o1.code, o2.code)
                }
            } else if(this.currentSort === 'name') {
                if(this.currentSortDir === 'asc'){
                    result = this.compareStringsAsc(o1.name, o2.name)
                } else {
                    result = this.compareStringsDesc(o1.name, o2.name)
                }
            } else if(this.currentSort === 'contractor') {
                if(this.currentSortDir === 'asc'){
                    result = this.compareStringsAsc(o1.contractor.name, o2.contractor.name)
                } else {
                    result = this.compareStringsDesc(o1.contractor.name, o2.contractor.name)
                }
            } else if(this.currentSort === 'unit') {
                if(this.currentSortDir === 'asc'){
                    result = this.compareStringsAsc(o1.unit.unit, o2.unit.unit)
                } else {
                    result = this.compareStringsDesc(o1.unit.unit, o2.unit.unit)
                }
            } else if(this.currentSort === 'pkg') {
                if(this.currentSortDir === 'asc'){
                    result = o1.quantityInSecondaryPackaging - o2.quantityInSecondaryPackaging
                } else {
                    result = o2.quantityInSecondaryPackaging - o1.quantityInSecondaryPackaging
                }
            } else if(this.currentSort === 'active') {
                var active1 = o1.active ? "Да" : "Нет";
                var active2 = o2.active ? "Да" : "Нет";
                if(this.currentSortDir === 'asc'){
                    result = this.compareStringsAsc(active1, active2)
                } else {
                    result = this.compareStringsDesc(active1, active2)
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