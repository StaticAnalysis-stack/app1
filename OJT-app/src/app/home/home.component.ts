import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor() { }

  carousel_img1="assets/carousel_img1.jpg";
  carousel_img2="assets/carousel_img2.jpg";
  carousel_img3="assets/carousel_img3.jpg";
  carousel_img4="assets/carousel_img4.jpg";


  card_1="assets/card_1.jpg";
  card_2="assets/card_2.jpg";
  card_3="assets/card_3.jpg";
  card_4="assets/card_4.jpg";
  card_5="assets/card_5.jpg";




  long_bar_1="assets/long_bar_1.jpg";
  long_bar_2="assets/long_bar_2.jpg";
  long_bar_3="assets/long_bar_3.jpg";
  long_bar_4="assets/long_bar_4.jpg";
  long_bar_5="assets/long_bar_5.jpg"
  long_bar_6="assets/long_bar_6.jpg"
  long_bar_7="assets/long_bar_7.jpg"






  ngOnInit(): void {
  }

}
