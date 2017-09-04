class CreateRooms < ActiveRecord::Migration[5.0]
  def change
    create_table :rooms do |t|
      t.string :region
      t.string :cafename
      t.string :roomname
      t.string :capacity
      t.string :theme
      t.string :level
      t.string :cafeimgurl
      t.string :roomimgurl
      t.string :addr
      t.string :etc

      t.timestamps
    end
  end
end
