class AddCntToRoom < ActiveRecord::Migration[5.0]
  def change
    add_column :rooms, :cnt, :integer
  end
end
