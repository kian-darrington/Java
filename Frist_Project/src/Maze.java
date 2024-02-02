import java.util.ArrayList;

public class Maze {
    Room[][] rooms;
    final int N = 0, E = 1, S = 2, W = 3;
    static int X_ROOMS, Y_ROOMS;
    Maze(Room[][] r) {
        rooms = r;
        X_ROOMS = rooms.length;
        if (X_ROOMS > 0)
            Y_ROOMS = rooms[0].length;
    }
    char block = 0x2588;
    void setRooms(Room[][] r) {
        rooms = r;
        X_ROOMS = rooms.length;
        if (Y_ROOMS > 0)
            Y_ROOMS = rooms[0].length;
    }
    Room[][] getRooms() {return rooms;}
    public void printMaze(){
        for (int i = 0; i < Y_ROOMS * 2 + 1; i++){
            for (int o = 0; o < X_ROOMS * 2 + 1; o++){
                if (o % 2 == 0 && i % 2 == 0)
                    System.out.print(block);
                else if (o % 2 == 1 && i % 2 == 1)
                    System.out.print(rooms[o / 2][i / 2]);
                else{
                    if (i % 2 == 0)
                        System.out.print(upDownCheck(o /2, i / 2));
                    else {
                        System.out.print(sideSideCheck(o /2, i / 2));
                    }
                }
            }
            System.out.println();
        }
    }
    public String justRooms(){
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < Y_ROOMS; i++){
            for (int o = 0; o < X_ROOMS; o++){
                string.append(rooms[o][i]);
            }
            if (i == 0)
                string.insert(0,"<html>");
            if (i == Y_ROOMS)
                string.append("<html>");
            else
                string.append("<br>");
        }
        return string.toString();
    }
    public String stringMaze(){
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < Y_ROOMS * 2 + 1; i++){
            for (int o = 0; o < X_ROOMS * 2 + 1; o++){
                if (o % 2 == 0 && i % 2 == 0)
                    string.append(block);
                else if (o % 2 == 1 && i % 2 == 1)
                    string.append(rooms[o / 2][i / 2]);
                else{
                    if (i % 2 == 0)
                        string.append(upDownCheck(o /2, i / 2));
                    else {
                        string.append(sideSideCheck(o /2, i / 2));
                    }
                }
            }
            if (i == 0)
                string.insert(0,"<html>");
            if (i == Y_ROOMS * 2)
                string.append("<html>");
            else
                string.append("<br>");
        }
        return string.toString();
    }
    //Checks the current room and down neighbor path of a room to determine what is printed
    String upDownCheck(int x, int y){
        if (y == 0)
            return ""+block;
        else if (y == Y_ROOMS)
            return ""+block;
        else if (y == Y_ROOMS - 1){
            if (rooms[x][y].getMove(N))
                return rooms[x][y].color();
            else
                return ""+block;
        }
        else if (rooms[x][y].getMove(N) && rooms[x][y - 1].getMove(S))
            return rooms[x][y].color();
        else
            return ""+block;
    }
    //Checks the current room and left neighbor path of a room to determine what is printed
    String sideSideCheck(int x, int y){
        if (x == 0)
            return ""+block;
        else if (x == X_ROOMS)
            return ""+block;
        else if (x == X_ROOMS - 1) {
            if (rooms[x][y].getMove(W))
                return rooms[x][y].color();
            else
                return ""+block;
        }
        else if (rooms[x][y].getMove(W) && rooms[x - 1][y].getMove(E))
            return rooms[x][y].color();
        else
            return ""+block;
    }
}
