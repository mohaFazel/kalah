import { Component, OnInit } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Board } from "./models/board";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.scss"]
})
export class AppComponent implements OnInit {
  board: Board = new Board();
  constructor(private http: HttpClient) {}

  ngOnInit() {}

  startGame() {
    this.http.post<Board>("http://localhost:8080/games", {}).subscribe(data => {
      this.board = data;
    });
  }

  getStatus(key: number) {
    return this.board.status.filter(s => s.key === key)[0];
  }

  move(pit: number) {
    this.http
      .put<Board>(
        "http://localhost:8080/games/"
          .concat(this.board.id)
          .concat("/pits/")
          .concat(String(pit)),
        {}
      )
      .subscribe(
        data => {
          this.board = data;
        },
        error => {
          alert(error.error);
        }
      );
  }
}
