import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-main-layout',
  templateUrl: './main-layout.component.html',
  styleUrls: ['./main-layout.component.scss']
})
export class MainLayoutComponent implements OnInit {

  isCollapsed: boolean;

  constructor() { }

  ngOnInit(): void {
  }
  public toggleCollapsed(event) {
    this.isCollapsed = event;
  }

}
