import java.util.Random;
class BinaryTree extends MazeGenerator
{
    static Random rand=new Random();

    public static void generate(Grid grid)
    {
        for(int i=0; i<grid.getRow(); i++)
            for(int j=0; j < grid.getCol(); j++)
            {
                int choice=rand.nextInt(1,3);
if(choice==1) grid.link(i, j, Direction.NORTH);
if(choice==2) grid.link(i, j, Direction.EAST);
if(i==0) grid.link(i, j, Direction.WEST);
if(j==grid.getCol()-1) grid.link(i, j, Direction.NORTH);

            }
    }
}
