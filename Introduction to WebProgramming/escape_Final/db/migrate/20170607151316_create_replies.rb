class CreateReplies < ActiveRecord::Migration[5.0]
  def change
    create_table :replies do |t|
      t.string :usr
      t.text :comment
      t.string :int
      t.string :rating

      t.timestamps
    end
  end
end
