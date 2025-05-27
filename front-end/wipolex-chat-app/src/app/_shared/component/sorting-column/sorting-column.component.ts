import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { SortDirection } from '../../../_models/SortingProperty';


@Component({
  selector: 'app-sorting-column',
  templateUrl: './sorting-column.component.html',
  host:{
    ['style.cursor']: 'enabled ? "pointer" : ""'
  }
})
export class SortingColumnComponent implements OnInit {
  @Input() public sorting: any;
  @Input() public sortingProperty: any;
  @Input() public enabled = true;
  @Output() change = new EventEmitter();
  public Direction = SortDirection;
  get isSorted () {
    return this.sorting?.sortingProperty === this.sortingProperty;
  }

  constructor() {}
  ngOnInit() {  }

  public sort() {
    if (this.enabled) {
      if (this.isSorted) {
        this.sorting.direction = this.sorting.direction === SortDirection.ASC ? SortDirection.DESC : SortDirection.ASC;
      } else {
        this.sorting.direction = SortDirection.ASC;
      }
      this.sorting.sortingProperty = this.sortingProperty;
      this.change.emit();
    }
  }
}
