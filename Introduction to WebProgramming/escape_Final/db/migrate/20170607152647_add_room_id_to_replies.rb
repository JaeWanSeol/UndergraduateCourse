class AddRoomIdToReplies < ActiveRecord::Migration[5.0]
  def change
    add_reference :replies, :room, foreign_key: true
  end
end
