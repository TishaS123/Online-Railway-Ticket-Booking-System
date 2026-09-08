import { SeatStatus } from "./seat-status.enum";

export class AddSeat{
  
        public seatNumber : string = '';
        public status : SeatStatus = SeatStatus.AVAILABLE;
        public coachId : number= 0;
    
}