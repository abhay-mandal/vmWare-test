import { Component, EventEmitter, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {

  isCollapsed: boolean = false;
  @Output() onClickCollapsed: EventEmitter<any> = new EventEmitter<any>();
  
  constructor() { }

  ngOnInit(): void {
  }

  toggleSidebar() {
    this.isCollapsed = !this.isCollapsed;
    this.onClickCollapsed.emit(this.isCollapsed);
  }

}
