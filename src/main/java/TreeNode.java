import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class TreeNode {

    List<List<Character>> board;
    int rate;
    String move;
    List<TreeNode> children = new LinkedList<>();

    TreeNode(List<List<Character>> board, int rate, String move){
        this.board = board;
        this.rate = rate;
        this.move = move;
    }

    TreeNode(List<List<Character>> board, int rate,String move, List<TreeNode> child){
        this.board = board;
        this.rate = rate;
        this.move = move;
        children = child;
    }
}
