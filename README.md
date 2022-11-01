# Bài tập lớn OOP - Bomberman Game
INT2204 21 N3

Đinh Văn Ninh - 21020362

Nguyễn Ngọc Linh - 21020774

Mô phỏng lại trò chơi [Bomberman](https://www.youtube.com/watch?v=mKIOVwqgSXM) kinh điển của NES.

<img src="res/sprites/level4.png" alt="drawing" width="400"/>

### Game có 5 level, độ khó tăng dần theo số level
- Game có thể chơi từ đầu(START), hoặc tiếp tục màn chơi cũ(CONTINUE), lịch sử điểm chơi cao nhất thể hiện ở TOP
- ![](res/sprites/menu.png)
- Chuyển giao giữa của level
- ![](res/sprites/stage1.png)
- Kết thúc game thắng hoặc thua
- ![](res/sprites/gameover.png)
- ![](res/sprites/win.png)
### Thanh thể hiện TIME, Số SCORE và LEFT
- ![](res/sprites/level.png)

## Mô tả về các đối tượng trong trò chơi

- ![](res/sprites/player_down.png) *Bomber* 
- ![](res/sprites/balloom_left1.png) *Enemy* 
- ![](res/sprites/bomb.png) *Bomb* 

- ![](res/sprites/grass.png) *Grass* 
- ![](res/sprites/wall.png) *Wall*
- ![](res/sprites/brick.png) *Brick*

- ![](res/sprites/portal.png) *Portal* 

Các *Item* cũng được giấu phía sau Brick và chỉ hiện ra khi Brick bị phá hủy. Bomber có thể sử dụng Item bằng cách di chuyển vào vị trí của Item. Thông tin về chức năng của các Item được liệt kê như dưới đây:
- ![](res/sprites/powerup_speed.png) *SpeedItem* Khi sử dụng Item này, Bomber sẽ được tăng vận tốc di chuyển thêm một giá trị thích hợp
- ![](res/sprites/powerup_flames.png) *FlameItem* Item này giúp tăng phạm vi ảnh hưởng của Bomb khi nổ (độ dài các Flame lớn hơn)
- ![](res/sprites/powerup_bombs.png) *BombItem* Thông thường, nếu không có đối tượng Bomb nào đang trong trạng thái kích hoạt, Bomber sẽ được đặt và kích hoạt duy nhất một đối tượng Bomb. Item này giúp tăng số lượng Bomb có thể đặt thêm một.

Có nhiều loại Enemy trong Bomberman, tuy nhiên trong phiên bản này chỉ yêu cầu cài đặt hai loại Enemy dưới đây (nếu cài đặt thêm các loại khác sẽ được cộng thêm điểm):
- ![](res/sprites/balloom_left1.png) *Balloom* 
- ![](res/sprites/oneal_left1.png) *Oneal* 
- ![](res/sprites/doll_left1.png) *Doll* 
- ![](res/sprites/kondoria_left1.png) *Kondoria*  
- ![](res/sprites/minvo_left1.png) *Minvo* 
- ![](res/sprites/Ovapee.png) *Ovape*


## Mô tả game play, xử lý va chạm và xử lý bom nổ
- Trong một màn chơi, Bomber sẽ được người chơi di chuyển, đặt và kích hoạt Bomb với mục tiêu chính là tiêu diệt tất cả Enemy và tìm ra vị trí Portal để có thể qua màn mới
- Bomber sẽ bị giết khi va chạm với Enemy hoặc thuộc phạm vi Bomb nổ. Lúc đấy trò chơi kết thúc.
- Enemy bị tiêu diệt khi thuộc phạm vi Bomb nổ
- Một đối tượng thuộc phạm vi Bomb nổ có nghĩa là đối tượng đó va chạm với một trong các tia lửa được tạo ra tại thời điểm một đối tượng Bomb nổ.

- Khi Bomb nổ, một Flame trung tâm![](res/sprites/bomb_exploded.png) tại vị trí Bomb nổ và bốn Flame tại bốn vị trí ô đơn vị xung quanh vị trí của Bomb xuất hiện theo bốn hướng trên![](res/sprites/explosion_vertical.png)/dưới![](res/sprites/explosion_vertical.png)/trái![](res/sprites/explosion_horizontal.png)/phải![](res/sprites/explosion_horizontal.png). Độ dài bốn Flame xung quanh mặc định là 1 đơn vị, được tăng lên khi Bomber sử dụng các FlameItem.
- Khi các Flame xuất hiện, nếu có một đối tượng thuộc loại Brick/Wall nằm trên vị trí một trong các Flame thì độ dài Flame đó sẽ được giảm đi để sao cho Flame chỉ xuất hiện đến vị trí đối tượng Brick/Wall theo hướng xuất hiện. Lúc đó chỉ có đối tượng Brick/Wall bị ảnh hưởng bởi Flame, các đối tượng tiếp theo không bị ảnh hưởng. Còn nếu vật cản Flame là một đối tượng Bomb khác thì đối tượng Bomb đó cũng sẽ nổ ngay lập tức.




